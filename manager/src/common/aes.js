import Vue from 'vue'
import CryptoJS from "crypto-js";

let AESkey = '3b5949e0c26b8776';
let AESiv = '7a4752a276de9570';

/**
 * 加密数据
 */
Vue.prototype.encrypt = function (data) {
    if (!(typeof data === 'string' && data.constructor === String)) {
        data = JSON.stringify(data);
    }
    let sendData = CryptoJS.enc.Utf8.parse(data);
    let key = CryptoJS.enc.Utf8.parse(AESkey);
    let iv = CryptoJS.enc.Utf8.parse(AESiv);
    let encrypted = CryptoJS.AES.encrypt(sendData, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.ZeroPadding
    });
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
};

/**
 * 解密数据
 */
Vue.prototype.decrypt = function (data) {
    let key = CryptoJS.enc.Utf8.parse(AESkey);
    let iv = CryptoJS.enc.Utf8.parse(AESiv);
    let decrypted = CryptoJS.AES.decrypt(data, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.ZeroPadding
    });
    decrypted = decrypted.toString(CryptoJS.enc.Utf8);
    let i = decrypted.indexOf('\0');
    if (i !== -1) {
        decrypted = decrypted.slice(0, i);
    }
    try {
        decrypted = JSON.parse(decrypted);
    } catch (e) {
        console.log(e);
    }
    return decrypted;
};