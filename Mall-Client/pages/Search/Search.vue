<template>
	<view class="searchpage">
		<view class="search_row">
			<input type="text" v-model:value="inputValue" placeholder="请输入商品关键字" @input="handleInput" />
			<button v-show="isFocus" @tap="handleCancel">取消</button>
		</view>
		<view class="search_content">
		  <navigator
		    class="search_item"
		    v-for="item in productList"
		    :key="item.id"
		    :url="'/pages/product_detail/product_detail?id='+item.id"
		  >
		  {{item.name}}
		  </navigator>
		</view>
	</view>
</template>

<script>
	import {
		requestUtil
	} from '../../utils/request.js';
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				productList: [], // 商品数组
				inputValue: "", // 输入框的值
				TimeoutId: -1,
				isFocus: false // 取消 按钮 是否显示
			};
		},
		methods: {
			// 处理input事件
			handleInput(e) {
				const {
					value
				} = e.detail;
				if (!value.trim()) {
					this.productList = [];
					this.isFocus = false;
					return;
				}
				this.isFocus = true;
				clearTimeout(this.TimeoutId);
				this.TimeoutId = setTimeout(() => {
					this.search(value)
				}, 1000)
			},
			
			  // 点击 取消按钮
			  handleCancel(){
			      this.productList = []; // 商品数组
			      this.inputValue =""; // 输入框的值
			      this.isFocus =false; // 取消 按钮 是否显示
			  },

			/**
			 * 获取商品详情
			 */
			async search(q) {
				const result = await requestUtil({
					url: '/product/search',
					data: {
						q
					}
				});
				this.productList = result.message;
			}
		}
	}
</script>

<style scoped lang="less">
	.searchpage {
		background-color: #F9F9F9;
		padding-top: 20rpx;
		height: 100vh;
	}

	.search_row {
		height: 50rpx;
		display: flex;

		input {
			background-color: #FFFFFF;
			flex: 1;
			height: 100%;
			padding-left: 30rpx;
		}

		button {
			display: flex;
			justify-content: center;
			align-items: center;
			width: 110rpx !important;
			font-size: 26rpx;
			padding: 0;
			margin: 0 10rpx;
			height: 100%;
		}
	}

	.search_content {
		margin-top: 15rpx;
		padding: 0px;

		.search_item {
			background-color: #FFFFFF;
			font-size: 26rpx;
			padding: 15rpx;
			align-items: center;
			border-bottom: 1rpx solid #EAEAEA;

			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
		}
	}
</style>
