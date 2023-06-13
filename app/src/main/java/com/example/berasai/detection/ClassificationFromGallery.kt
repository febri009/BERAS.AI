package com.example.berasai.detection

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.util.*

class ClassificationFromGallery(assetManager: AssetManager, modelPath: String, labelPath: String, private val inputSize: Int) {
    private var interpreter: Interpreter
    private var labelList: List<String>
    private val pixelSize: Int = 3
    private val imageMean = 0
    private val imageStd = 255.0f
    private val maxResults = 3
    private val threshold = 0.4f

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F,
        var percent: Float = confidence * 100
    ) {
        override fun toString(): String {
            val roundedPercent = String.format("%.2f", percent)
            return "Title = $title, Hasil Prediksi = $roundedPercent"
        }
    }

    init {
        val options = Interpreter.Options()
        interpreter = Interpreter(loadModelFile(assetManager, modelPath), options)
        labelList = loadLabelList(assetManager, labelPath)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor).channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return inputStream.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }

    fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val result = Array(BATCH_SIZE) { FloatArray(labelList.size) }
        interpreter.run(byteBuffer, result)
        return getSortedResult(result)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * pixelSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val `val` = intValues[pixel++]

                byteBuffer.putFloat((((`val`.shr(16) and 0xFF) - imageMean) / imageStd))
                byteBuffer.putFloat((((`val`.shr(8) and 0xFF) - imageMean) / imageStd))
                byteBuffer.putFloat((((`val` and 0xFF) - imageMean) / imageStd))
            }
        }
        return byteBuffer
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<Recognition> {
        Log.d("KlasifikasiDariGaleri", "List Size: (%d, %d, %d)".format(labelProbArray.size, labelProbArray[0].size, labelList.size))

        val pq = PriorityQueue(
            maxResults,
            Comparator<Recognition> { (_, _, confidence1), (_, _, confidence2) ->
                confidence1.compareTo(confidence2) * -1
            })

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= threshold) {
                pq.add(
                    Recognition(
                        "" + i,
                        if (labelList.size > i) labelList[i] else "Unknown",
                        confidence
                    )
                )
            }
        }
        Log.d("KlasifikasiDariGaleri", "pqsize: (%d)".format(pq.size))

        val recognitions = ArrayList<Recognition>()
        val recognitionsSize = pq.size.coerceAtMost(maxResults)
        val decimalFormat = DecimalFormat("0.00")
        for (i in 0 until recognitionsSize) {
            pq.poll()?.let { recognition ->
                val roundedConfidence = decimalFormat.format(recognition.percent).replace(',', '.')
                recognition.percent = roundedConfidence.toFloat()
                recognitions.add(recognition)
            }
        }
        return recognitions
    }

    companion object {
        private const val BATCH_SIZE = 1
    }
}



