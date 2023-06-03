package com.example.berasai

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Klasifikasi(private val assetManager: AssetManager) {

    private val labels: List<String>
    private val model: Interpreter

    init {
        val options = Interpreter.Options()
        model = Interpreter(getModelByteBuffer(assetManager), options)
        labels = getLabels(assetManager)
    }


    fun recognize(data: ByteArray): List<KlasifikasiResult> {
        val result = Array(1) { FloatArray(labels.size) }

        val unscaledBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        val bitmap = Bitmap.createScaledBitmap(unscaledBitmap, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE, false)

        val byteBuffer = ByteBuffer
            .allocateDirect(BATCH_SIZE * MODEL_INPUT_SIZE * MODEL_INPUT_SIZE * BYTES_PER_CHANNEL)
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

    private fun parseResults(result: Array<FloatArray>): List<KlasifikasiResult> {
        val recognitions = mutableListOf<KlasifikasiResult>()

        labels.forEachIndexed { index, label ->
            val probability = result[0][index]
            recognitions.add(KlasifikasiResult(label, probability))
        }

        return recognitions.sortedByDescending { it.probability }
    }

    private fun getModelByteBuffer(assetManager: AssetManager): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(MODEL_PATH)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun getLabels(assetManager: AssetManager): List<String> {
        val labels = ArrayList<String>()
        val reader = BufferedReader(InputStreamReader(assetManager.open(LABELS_PATH)))
        while (true) {
            val label = reader.readLine() ?: break
            labels.add(label)
        }
        reader.close()
        return labels
    }


    data class KlasifikasiResult(val label: String, val probability: Float)

    companion object {
        private const val BATCH_SIZE = 1 // process only 1 image at a time
        private const val MODEL_INPUT_SIZE = 224 // 224x224
        private const val BYTES_PER_CHANNEL = 4 // float size

        private const val LABELS_PATH = "Klasifikasi_Kualitas_Beras.txt"
        private const val MODEL_PATH = "Klasifikasi_Kualitas_Beras_VGG16Net_nf.tflite"
    }
}

