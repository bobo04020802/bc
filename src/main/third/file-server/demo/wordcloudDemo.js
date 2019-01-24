const fs = require('fs')
const WordCloud = require('../lib/wordcloud')
const { createCanvas, loadImage } = require('canvas')

let list = [["Love", 12], ["Liebe", 5], ["ፍቅር", 5], ["Lufu", 5], ["حب", 5], ["Aimor", 5], ["Amor", 5], ["Heyran", 5], ["ভালোবাসা", 5], ["Каханне", 5], ["Любоў", 5], ["Любов", 5], ["བརྩེ་དུང་།", 5], ["Ljubav", 5], ["Karantez", 5], ["Юрату", 5], ["Láska", 5], ["Amore", 5], ["Cariad", 5], ["Kærlighed", 5], ["Armastus", 5], ["Αγάπη", 5], ["Amo", 5], ["Amol", 5], ["Maitasun", 5], ["عشق", 5], ["Pyar", 5], ["Amour", 5], ["Leafde", 5], ["Gràdh", 5], ["愛", 5], ["爱", 5], ["પ્રેમ", 5], ["사랑", 5], ["Սեր", 5], ["Ihunanya", 5], ["Cinta", 5], ["ᑕᑯᑦᓱᒍᓱᑉᐳᖅ", 5], ["Ást", 5], ["אהבה", 5], ["ಪ್ರೀತಿ", 5], ["სიყვარული", 5], ["Махаббат", 5], ["Pendo", 5], ["Сүйүү", 5], ["Mīlestība", 5], ["Meilė", 5], ["Leefde", 5], ["Bolingo", 5], ["Szerelem", 5], ["Љубов", 5], ["സ്നേഹം", 5], ["Imħabba", 5], ["प्रेम", 5], ["Ái", 5], ["Хайр", 5], ["အချစ်", 5], ["Tlazohtiliztli", 5], ["Liefde", 5], ["माया", 5], ["मतिना", 5], ["Kjærlighet", 5], ["Kjærleik", 5], ["ପ୍ରେମ", 5], ["Sevgi", 5], ["ਪਿਆਰ", 5], ["پیار", 5], ["Miłość", 5], ["Leevde", 5], ["Dragoste", 5], ["Khuyay", 5], ["Любовь", 5], ["Таптал", 5], ["Dashuria", 5], ["Amuri", 5], ["ආදරය", 5], ["Ljubezen", 5], ["Jaceyl", 5], ["خۆشەویستی", 5], ["Љубав", 5], ["Rakkaus", 5], ["Kärlek", 5], ["Pag-ibig", 5], ["காதல்", 5], ["ప్రేమ", 5], ["ความรัก", 5], ["Ишқ", 5], ["Aşk", 5], ["محبت", 5], ["Tình yêu", 5], ["Higugma", 5], ["ליבע", 5], ["Liebe", 4], ["ፍቅር", 4], ["Lufu", 4], ["حب", 4], ["Aimor", 4], ["Amor", 4], ["Heyran", 4], ["ভালোবাসা", 4], ["Каханне", 4], ["Любоў", 4], ["Любов", 4], ["བརྩེ་དུང་།", 4], ["Ljubav", 4], ["Karantez", 4], ["Юрату", 4], ["Láska", 4], ["Amore", 4], ["Cariad", 4], ["Kærlighed", 4], ["Armastus", 4], ["Αγάπη", 4], ["Amo", 4], ["Amol", 4], ["Maitasun", 4], ["عشق", 4], ["Pyar", 4], ["Amour", 4], ["Leafde", 4], ["Gràdh", 4], ["愛", 4], ["爱", 4], ["પ્રેમ", 4], ["사랑", 4], ["Սեր", 4], ["Ihunanya", 4], ["Cinta", 4], ["ᑕᑯᑦᓱᒍᓱᑉᐳᖅ", 4], ["Ást", 4], ["אהבה", 4], ["ಪ್ರೀತಿ", 4], ["სიყვარული", 4], ["Махаббат", 4], ["Pendo", 4], ["Сүйүү", 4], ["Mīlestība", 4], ["Meilė", 4], ["Leefde", 4], ["Bolingo", 4], ["Szerelem", 4], ["Љубов", 4], ["സ്നേഹം", 4], ["Imħabba", 4], ["प्रेम", 4], ["Ái", 4], ["Хайр", 4], ["အချစ်", 4], ["Tlazohtiliztli", 4], ["Liefde", 4], ["माया", 4], ["मतिना", 4], ["Kjærlighet", 4], ["Kjærleik", 4], ["ପ୍ରେମ", 4], ["Sevgi", 4], ["ਪਿਆਰ", 4], ["پیار", 4], ["Miłość", 4], ["Leevde", 4], ["Dragoste", 4], ["Khuyay", 4], ["Любовь", 4], ["Таптал", 4], ["Dashuria", 4], ["Amuri", 4], ["ආදරය", 4], ["Ljubezen", 4], ["Jaceyl", 4], ["خۆشەویستی", 4], ["Љубав", 4], ["Rakkaus", 4], ["Kärlek", 4], ["Pag-ibig", 4], ["காதல்", 4], ["ప్రేమ", 4], ["ความรัก", 4], ["Ишқ", 4], ["Aşk", 4], ["محبت", 4], ["Tình yêu", 4], ["Higugma", 4], ["ליבע", 4], ["Liebe", 3], ["ፍቅር", 3], ["Lufu", 3], ["حب", 3], ["Aimor", 3], ["Amor", 3], ["Heyran", 3], ["ভালোবাসা", 3], ["Каханне", 3], ["Любоў", 3], ["Любов", 3], ["བརྩེ་དུང་།", 3], ["Ljubav", 3], ["Karantez", 3], ["Юрату", 3], ["Láska", 3], ["Amore", 3], ["Cariad", 3], ["Kærlighed", 3], ["Armastus", 3], ["Αγάπη", 3], ["Amo", 3], ["Amol", 3], ["Maitasun", 3], ["عشق", 3], ["Pyar", 3], ["Amour", 3], ["Leafde", 3], ["Gràdh", 3], ["愛", 3], ["爱", 3], ["પ્રેમ", 3], ["사랑", 3], ["Սեր", 3], ["Ihunanya", 3], ["Cinta", 3], ["ᑕᑯᑦᓱᒍᓱᑉᐳᖅ", 3], ["Ást", 3], ["אהבה", 3], ["ಪ್ರೀತಿ", 3], ["სიყვარული", 3], ["Махаббат", 3], ["Pendo", 3], ["Сүйүү", 3], ["Mīlestība", 3], ["Meilė", 3], ["Leefde", 3], ["Bolingo", 3], ["Szerelem", 3], ["Љубов", 3], ["സ്നേഹം", 3], ["Imħabba", 3], ["प्रेम", 3], ["Ái", 3], ["Хайр", 3], ["အချစ်", 3], ["Tlazohtiliztli", 3], ["Liefde", 3], ["माया", 3], ["मतिना", 3], ["Kjærlighet", 3], ["Kjærleik", 3], ["ପ୍ରେମ", 3], ["Sevgi", 3], ["ਪਿਆਰ", 3], ["پیار", 3], ["Miłość", 3], ["Leevde", 3], ["Dragoste", 3], ["Khuyay", 3], ["Любовь", 3], ["Таптал", 3], ["Dashuria", 3], ["Amuri", 3], ["ආදරය", 3], ["Ljubezen", 3], ["Jaceyl", 3], ["خۆشەویستی", 3], ["Љубав", 3], ["Rakkaus", 3], ["Kärlek", 3], ["Pag-ibig", 3], ["காதல்", 3], ["ప్రేమ", 3], ["ความรัก", 3], ["Ишқ", 3], ["Aşk", 3], ["محبت", 3], ["Tình yêu", 3], ["Higugma", 3], ["ליבע", 3], ["Liebe", 2], ["ፍቅር", 2], ["Lufu", 2], ["حب", 2], ["Aimor", 2], ["Amor", 2], ["Heyran", 2], ["ভালোবাসা", 2], ["Каханне", 2], ["Любоў", 2], ["Любов", 2], ["བརྩེ་དུང་།", 2], ["Ljubav", 2], ["Karantez", 2], ["Юрату", 2], ["Láska", 2], ["Amore", 2], ["Cariad", 2], ["Kærlighed", 2], ["Armastus", 2], ["Αγάπη", 2], ["Amo", 2], ["Amol", 2], ["Maitasun", 2], ["عشق", 2], ["Pyar", 2], ["Amour", 2], ["Leafde", 2], ["Gràdh", 2], ["愛", 2], ["爱", 2], ["પ્રેમ", 2], ["사랑", 2], ["Սեր", 2], ["Ihunanya", 2], ["Cinta", 2], ["ᑕᑯᑦᓱᒍᓱᑉᐳᖅ", 2], ["Ást", 2], ["אהבה", 2], ["ಪ್ರೀತಿ", 2], ["სიყვარული", 2], ["Махаббат", 2], ["Pendo", 2], ["Сүйүү", 2], ["Mīlestība", 2], ["Meilė", 2], ["Leefde", 2], ["Bolingo", 2], ["Szerelem", 2], ["Љубов", 2], ["സ്നേഹം", 2], ["Imħabba", 2], ["प्रेम", 2], ["Ái", 2], ["Хайр", 2], ["အချစ်", 2], ["Tlazohtiliztli", 2], ["Liefde", 2], ["माया", 2], ["मतिना", 2], ["Kjærlighet", 2], ["Kjærleik", 2], ["ପ୍ରେମ", 2], ["Sevgi", 2], ["ਪਿਆਰ", 2], ["پیار", 2], ["Miłość", 2], ["Leevde", 2], ["Dragoste", 2], ["Khuyay", 2], ["Любовь", 2], ["Таптал", 2], ["Dashuria", 2], ["Amuri", 2], ["ආදරය", 2], ["Ljubezen", 2], ["Jaceyl", 2], ["خۆشەویستی", 2], ["Љубав", 2], ["Rakkaus", 2], ["Kärlek", 2], ["Pag-ibig", 2], ["காதல்", 2], ["ప్రేమ", 2], ["ความรัก", 2], ["Ишқ", 2], ["Aşk", 2], ["محبت", 2], ["Tình yêu", 2], ["Higugma", 2], ["ליבע", 2], ["Liebe", 2], ["ፍቅር", 2], ["Lufu", 2], ["حب", 2], ["Aimor", 2], ["Amor", 2], ["Heyran", 2], ["ভালোবাসা", 2], ["Каханне", 2], ["Любоў", 2], ["Любов", 2], ["བརྩེ་དུང་།", 2], ["Ljubav", 2], ["Karantez", 2], ["Юрату", 2], ["Láska", 2], ["Amore", 2], ["Cariad", 2], ["Kærlighed", 2], ["Armastus", 2], ["Αγάπη", 2], ["Amo", 2], ["Amol", 2], ["Maitasun", 2], ["عشق", 2], ["Pyar", 2], ["Amour", 2], ["Leafde", 2], ["Gràdh", 2], ["愛", 2], ["爱", 2], ["પ્રેમ", 2], ["사랑", 2], ["Սեր", 2], ["Ihunanya", 2], ["Cinta", 2], ["ᑕᑯᑦᓱᒍᓱᑉᐳᖅ", 2], ["Ást", 2], ["אהבה", 2], ["ಪ್ರೀತಿ", 2], ["სიყვარული", 2], ["Махаббат", 2], ["Pendo", 2], ["Сүйүү", 2], ["Mīlestība", 2], ["Meilė", 2], ["Leefde", 2], ["Bolingo", 2], ["Szerelem", 2], ["Љубов", 2], ["സ്നേഹം", 2], ["Imħabba", 2], ["प्रेम", 2], ["Ái", 2], ["Хайр", 2], ["အချစ်", 2], ["Tlazohtiliztli", 2], ["Liefde", 2], ["माया", 2], ["मतिना", 2], ["Kjærlighet", 2], ["Kjærleik", 2], ["ପ୍ରେମ", 2], ["Sevgi", 2], ["ਪਿਆਰ", 2], ["پیار", 2], ["Miłość", 2], ["Leevde", 2], ["Dragoste", 2], ["Khuyay", 2], ["Любовь", 2], ["Таптал", 2], ["Dashuria", 2], ["Amuri", 2], ["ආදරය", 2], ["Ljubezen", 2], ["Jaceyl", 2], ["خۆشەویستی", 2], ["Љубав", 2], ["Rakkaus", 2], ["Kärlek", 2], ["Pag-ibig", 2], ["காதல்", 2], ["ప్రేమ", 2], ["ความรัก", 2], ["Ишқ", 2], ["Aşk", 2], ["محبت", 2], ["Tình yêu", 2], ["Higugma", 2], ["ליבע", 2]]
let options = {
    list: list,
    gridSize: 1,
    weightFactor: 2,
    //fontFamily: 'Hiragino Mincho Pro, serif',
    color: 'random-dark',
    backgroundColor: '#f0f0f0',
    rotateRatio: 0.5,
    //shape: 'star',
    imageShape: 'http://localhost:3000/images/7ab/f28/7abf28c23ea19d443e80703d49473f88.png'
};
(async () => {
    let canvas = createCanvas(800, 600)
    await renderWordCloud(canvas, options)
    fs.writeFileSync('./demo/wordcloud.png', canvas.toBuffer())
})()



async function renderWordCloud(canvas, options) {
    if (options.imageShape) {
        let imageShapeCanvas = await drawImageShape(options.imageShape)
        options.clearCanvas = false
        let ctx = canvas.getContext('2d')
        ctx.fillStyle = options.backgroundColor || '#fff';
        ctx.fillRect(0, 0, 1, 1);
        let bgPixel = ctx.getImageData(0, 0, 1, 1).data
        ctx.drawImage(imageShapeCanvas,
            0, 0, imageShapeCanvas.width, imageShapeCanvas.height,
            0, 0, canvas.width, canvas.height)
        let imageData = ctx.getImageData(0, 0, canvas.width, canvas.height)
        let newImageData = ctx.createImageData(imageData)
        for (let i = 0; i < imageData.data.length; i += 4) {
            if (imageData.data[i + 3] > 128) {
                newImageData.data[i] = bgPixel[0];
                newImageData.data[i + 1] = bgPixel[1];
                newImageData.data[i + 2] = bgPixel[2];
                newImageData.data[i + 3] = bgPixel[3];
            } else {
                // This color must not be the same w/ the bgPixel.
                newImageData.data[i] = bgPixel[0];
                newImageData.data[i + 1] = bgPixel[1];
                newImageData.data[i + 2] = bgPixel[2];
                newImageData.data[i + 3] = bgPixel[3] ? (bgPixel[3] - 1) : 0;
            }
        }
        ctx.putImageData(newImageData, 0, 0)
        WordCloud(canvas, options)
    } else {
        WordCloud(canvas, options)
    }

}

async function drawImageShape(src) {
    let img = await loadImage(src)

    imageShapeCanvas = createCanvas()
    imageShapeCanvas.width = img.width
    imageShapeCanvas.height = img.height
    let ctx = imageShapeCanvas.getContext('2d')
    ctx.drawImage(img, 0, 0, img.width, img.height)

    let imageData = ctx.getImageData(0, 0, imageShapeCanvas.width, imageShapeCanvas.height)
    let newImageData = ctx.createImageData(imageData)

    for (let i = 0; i < imageData.data.length; i += 4) {
        let tone = imageData.data[i] +
            imageData.data[i + 1] +
            imageData.data[i + 2]
        let alpha = imageData.data[i + 3]

        if (alpha < 128 || tone > 128 * 3) {
            // Area not to draw
            newImageData.data[i] =
                newImageData.data[i + 1] =
                newImageData.data[i + 2] = 255
            newImageData.data[i + 3] = 0
        } else {
            // Area to draw
            newImageData.data[i] =
                newImageData.data[i + 1] =
                newImageData.data[i + 2] = 0
            newImageData.data[i + 3] = 255
        }
    }
    ctx.putImageData(newImageData, 0, 0)

    return imageShapeCanvas
}