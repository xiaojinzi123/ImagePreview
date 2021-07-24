import net.coobird.thumbnailator.Thumbnails
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO
import kotlin.math.max


fun main() {

    val targetFolder = File("/Users/xiaojinzi/Documents/code/temp/images/")
    val targetFileList = targetFolder
        .listFiles()
        .filter { it.isFile }
    targetFileList.forEach { targetFile ->
        try {
            compressImage(targetFile = targetFile)
        } catch (e: Exception) {
            println("文件'${targetFile.path}'处理失败")
        }
    }

}

private fun compressImage(targetFile: File) {
    val targetSize = 800
    if (!targetFile.exists()) {
        return
    }
    val bufferedImage: BufferedImage = ImageIO.read(FileInputStream(targetFile))
    val targetScaleSize: Float =
        1f / max(bufferedImage.width.toFloat() / targetSize, bufferedImage.height.toFloat() / targetSize)
    val targetFileName = targetFile.name
    if (targetFileName.contains(other = "_thumbnail", ignoreCase = true)) {
        println("文件'${targetFile.path}'已经是缩略图")
        return
    }
    val tempIndex = targetFileName.lastIndexOf('.')
    // 如果有, 是包括 . 获取到的
    val targetSuffix = if (tempIndex > -1) {
        targetFileName.substring(tempIndex)
    } else {
        ""
    }
    val targetSimpleName = if (tempIndex > -1) {
        targetFileName.substring(0, tempIndex)
    } else {
        targetFileName
    }
    val resultFile = File(
        targetFile.parentFile,
        targetSimpleName + "_thumbnail" + targetSuffix
    )
    if (!resultFile.exists()) {
        Thumbnails.of(targetFile)
            .scale(targetScaleSize.toDouble())
            .outputQuality(0.5)
            .toFile(resultFile)
    } else {
        println("文件'${resultFile.path}'已存在")
    }

}