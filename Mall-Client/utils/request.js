import {
	rejects
} from "assert"
import {
	apiBaseUrl
} from "./config.js"
//对象
const post = (url, data = "", callback, complete) => {
	console.log(url, data, callback, complete)
	uni.showLoading({
		title: "加载中"
	})
	uni.request({
		url,
		data: data,
		header: {
			'Accept': "application/json",
			"Content-Type": "application/json"
		},
		method: "POST",
		success: (response) => {
			const result = response.data
			callback(result)
		},
		complete: (response) => {
			setTimeout(function() {
				uni.hideLoading();
			}, 1000)
			complete ? complete() : "";
		},
		fail: (error) => {
			console.log("网络开小差了")
			uni.showLoading({
				title: "网络开小差了"
			})

			uni.hideLoading()
			if (error && error.response) {
				showError(error.response)
			} else {
				//....
			}
		}
	})
}
//对象

const get = (url, callback) => {
	uni.showLoading({
		title: "加载中"
	})
	uni.request({
		url,
		header: {
			'Accept': "application/json",
			"Content-Type": "application/x-www-form-urlencoded",
			"cookie": uni.getStorageSync("cookie")
		},
		method: "GET",
		success: (response) => {
			const result = response.data
			callback(result)
		},
		complete: (response) => {
			setTimeout(function() {
				uni.hideLoading();
			}, 300)
		},
		fail: (error) => {
			uni.hideLoading()
			if (error && error.response) {
				showError(error.response)
			} else {
				//....
			}
		}
	})
}
//封装showError 
const showError = error => {
	let errorMsg = "";
	switch (error.status) {
		case 400:
			errorMsg = "请求参数错误"
			break
		case 401:
			errorMsg = "未授权，请登录"
			break
		case 403:
			errorMsg = "跨域拒绝访问"
			break
		case 404:
			errorMsg = `请求地址出错:${error.config.url}`
			break
		case 408:
			errorMsg = "请求超时"
			break
		case 500:
			errorMsg = "服务器错误"
			break
		case 502:
			errorMsg = "网关错误"
			break
		case 503:
			errorMsg = "服务不可用"
			break
		case 504:
			errorMsg = "网关超时"
			break
		case 505:
			errorMsg = "HTTP版本不受支持"
			break
		default:
			errorMsg = error.msg
			break
	}

	uni.showToast({
		title: errorMsg,
		icon: "none",
		duration: 1000,
		complete: () => {
			setTimeout(function() {
				uni.hideToast();
			}, 1000)
		}
	})
}


//wx login封装
export const getWxLogin = () => {
	return new Promise((resolve, reject) => {
		uni.login({
			timeout: 5000,
			success: (res) => {
				resolve(res)
			},
			fail: (err) => {
				reject(err)
			}
		})
	});
}

//wx getUserProfile封装
export const getUserProfile = () => {
	return new Promise((resolve, reject) => {
		uni.getUserProfile({
			desc: '获取用户信息',
			success: (res) => {
				resolve(res)
			},
			fail: (err) => {
				reject(err)
			}
		})
	});
}

/**
 * promise形式的 小程序微信支付
 */
export const requestPay = (pay) => {
	return new Promise((resolve, reject) => {
		uni.requestPayment({
			...pay,
			success: (res) => {
				console.log('paysuc')
				resolve(res)
			},
			fail: (err) => {
				reject()
			}
		})
	});
}

export const requestUtil = (params) => {
	// 判断url中是否带有 /my/ 请求的是私有的路径 带上header token
	let header = {
		...params.header
	};
	if (params.url.includes("/my/")) {
		// 拼接header 带上token
		header["token"] = wx.getStorageSync('token')
	}
	return new Promise((resolve, reject) => {
		uni.showLoading({
			title: "加载中"
		})
		uni.request({
			...params,
			header,
			url: apiBaseUrl + params.url,
			success: (response) => {
				const result = response.data
				resolve(result)
			},
			complete: (response) => {
				setTimeout(function() {
					uni.hideLoading();
				}, 300)
			},
			fail: (error) => {
				uni.showLoading({
					title: "网络开小差了"
				})
				uni.hideLoading()
				if (error && error.response) {
					showError(error.response)
				} else {
					//....
				}
			}
		})
	})
}
//手机号登录
export const login = (phone, password, callback) => get(apiBaseUrl + "/login/cellphone?phone=" + phone + "&password=" +
	password, callback)
//首页轮播图
export const findSwiper = (callback) => get(apiBaseUrl + "/product/findSwiper", callback)
//获取主页商品大分类
export const getBigType = (callback) => get(apiBaseUrl + "/bigType/findAll", callback)
//获取热卖商品
export const getHotProduct = (callback) => get(apiBaseUrl + "/product/findHot", callback)
//获取详细分类
export const getSmallType = (callback) => get(apiBaseUrl + "/bigType/findCategories", callback)
//获取商品详情信息
export const getProductDetail = (callback, id) => get(apiBaseUrl + "/product/detail?id=" + id, callback)
