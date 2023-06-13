package com.example.berasai.detection

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class ClassificationFromCamera(assetManager: AssetManager) {

    private val label: List<String>
    private val model: Interpreter
    data class Recognition(
        val name: String,
        val probability: Float
    ) {
        override fun toString() =
            "$name, akurasi : ${probability}%"
    }

    init {
        model = Interpreter(getModelByteBuffer(assetManager, MODEL_PATH))
        label = getLabels(assetManager, LABELS_PATH)
    }

    fun recognize(data: ByteArray): List<Recognition> {
        val result = Array(BATCH_SIZE) { FloatArray(label.size) }

        val unscaledBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        val bitmap =
            Bitmap.createScaledBitmap(unscaledBitmap, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE, false)

        val byteBuffer = ByteBuffer
            .allocateDirect(
                BATCH_SIZE *
                        MODEL_INPUT_SIZE *
                        MODEL_INPUT_SIZE *
                        BYTES_PER_CHANNEL *
                        PIXEL_SIZE
            )
            .apply { order(ByteOrder.nativeOrder()) }

        val pixelValues = IntArray(MODEL_INPUT_SIZE * MODEL_INPUT_SIZE)
        bitmap.getPixels(pixelValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until MODEL_INPUT_SIZE) {
            for (j in 0 until MODEL_INPUT_SIZE) {
                val pixelValue = pixelValues[pixel++]
                byteBuffer.putFloat((pixelValue shr 16 and 0xFF) / 255f)
                byteBuffer.putFloat((pixelValue shr 8 and 0xFF) / 255f)
                byteBuffer.putFloat((pixelValue and 0xFF) / 255f)
            }
        }

        model.run(byteBuffer, result)
        return parseResults(result)
    }

    private fun parseResults(result: Array<FloatArray>): List<Recognition> {
        val recognitions = mutableListOf<Recognition>()

        label.forEachIndexed { index, label ->
            val probability = result[0][index]
            val roundedProbability = (probability * 100).roundTo(2)
            recognitions.add(Recognition(label, roundedProbability))
        }
        return recognitions.sortedByDescending { it.probability }
    }

    private fun Float.roundTo(decimalPlaces: Int): Float {
        val factor = 10.0.pow(decimalPlaces.toDouble()).toFloat()
        return (this * factor).roundToInt() / factor
    }

    @Throws(IOException::class)
    private fun getModelByteBuffer(assetManager: AssetManager, modelPath: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            .asReadOnlyBuffer()
    }

    @Throws(IOException::class)
    private fun getLabels(assetManager: AssetManager, labelPath: String): List<String> {
        val labels = ArrayList<String>()
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        while (true) {
            val label = reader.readLine() ?: break
            labels.add(label)
        }
        reader.close()
        return labels
    }

    companion object {
        private const val BATCH_SIZE = 1
        private const val MODEL_INPUT_SIZE = 224
        private const val BYTES_PER_CHANNEL = 4
        private const val PIXEL_SIZE = 3
        private const val LABELS_PATH = "Klasifikasi_Kualitas_Beras.txt"
        private const val MODEL_PATH = "Model_Kualitas_Beras_MobileNet.tflite"
    }
}


