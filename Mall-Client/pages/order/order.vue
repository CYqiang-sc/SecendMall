<template>
	<view>
		<view class="head-nav">
			<view class="title_item" v-for="item in tabs" :data-index = 'item.id' @click="handleTabsItemChange" :class="QueryParams.type==item.id?'activite':''">{{item.value}}</view>
		</view>
			<view class="order_main">
				<view v-for="item in orders" :key="item.id" class="order_item">
					<view class="order_no_row">
						<view class="order_no_text">订单编号</view>
						<view class="order_no_value">{{item.orderNo}}</view>
					</view>
					<view class="order_price_row">
						<view class="order_price_text">订单价格</view>
						<view class="order_price_value">¥{{item.totalPrice}}</view>
					</view>
					<view class="order_time_row">
						<view class="order_time_text">订单日期</view>
						<view class="order_time_value">{{item.createDate}}</view>
					</view>
				</view>
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
				// 接口参数
				QueryParams: {
					type: 0,
					page: 1, // 第几页
					pageSize: 10 // 每页记录数
				},
				// 总页数
				totalPage: 1,
				orders: [],
				tabs: [{
						id: 0,
						value: "全部订单",
						isActive: true
					},
					{
						id: 1,
						value: "待付款",
						isActive: false
					},
					{
						id: 2,
						value: "待收货",
						isActive: false
					},
				 {
						id: 3,
						value: "退款/退货",
						isActive: false
					},
				]
			};
		},
		methods: {
			// 根据标题索引激活选中的标签
			changeTitleByIndex(index) {
				let {
					tabs
				} = this.$data;
				tabs.forEach((v, i) => i == index ? v.isActive = true : v.isActive = false);

				this.tabs = tabs;
			},

			/**
			 * tab点击事件处理
			 * @param {} e 
			 */
			handleTabsItemChange(e) {
				//const {index}=e.currentTarget.dataset;
				const {
					index
				} = e.currentTarget.dataset;
				// 切换标题
				this.changeTitleByIndex(index);
				// 获取订单列表
				this.QueryParams.type = index;
				this.QueryParams.page = 1;
				this.orders = [];
				this.getOrders();
			},

			/**
			 * 获取订单
			 */
			async getOrders() {
				const res = await requestUtil({
					url: '/my/order/list',
					data: this.QueryParams
				});
				this.totalPage = res.totalPage;
				this.orders = [...this.$data.orders, ...res.orderList];
			}
		},
		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {
			let pages = getCurrentPages();
			let currentPage = pages[pages.length - 1];
			const {
				type
			} = currentPage.options;
			this.changeTitleByIndex(type)
			this.QueryParams.type = type;
			this.QueryParams.page = 1;
			this.orders = [];
			this.getOrders();
		},

		/**
		 * 生命周期函数--监听页面隐藏
		 */
		onHide: function() {
		},
		/**
		 * 生命周期函数--监听页面卸载
		 */
		onUnload: function() {
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			this.QueryParams.page = 1;
			this.orders = [];
			this.getOrders();
			// 手动关闭等待效果
			wx.stopPullDownRefresh({

			})
		},
		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {
			if (this.QueryParams.page >= this.totalPage) {
				// 没有下一页数据
				wx.showToast({
					title: '没有下一页数据了',
				})
			} else {
				this.QueryParams.page++;
				this.getOrders();
			}
		},
		
		/**
		 * 用户点击右上角分享
		 */
		onShareAppMessage: function() {
		
		}

	}
</script>

<style lang="less">
	.head-nav{
		  display: flex;
		.title_item{
			display: flex;
			justify-content: center;
			align-items: center;
			flex:1;
			padding: 15rpx 0;
		}
		.activite{
			color: #FF740B;
			border-bottom: 5rpx solid currentColor;
		
		}
	}
	.order_main {
		.order_item {
			padding: 20rpx;
			border-bottom: 1rpx solid #ccc;
			color: #666;

			.order_no_row {
				display: flex;
				justify-content: space-between;
				padding: 10rpx 0;

				.order_no_text {}

				.order_no_value {}
			}

			.order_price_row {
				display: flex;
				justify-content: space-between;
				padding: 10rpx 0;

				.order_price_text {}

				.order_price_value {
					color: var(--themeColor);
					font-size: 32rpx;
				}
			}

			.order_time_row {
				display: flex;
				justify-content: space-between;
				padding: 10rpx 0;

				.order_time_text {}

				.order_time_value {}
			}
		}
	}
</style>
