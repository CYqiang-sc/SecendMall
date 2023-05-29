<template>
	<view class="cates">
	  <!-- 搜索框开始 -->
	    <SearchBar></SearchBar>
	  <!-- 搜索框结束 -->
	
	  <view class="cates_container">
	    <!-- 左侧菜单 开始 -->
	      <scroll-view scroll-y class="left_menu">
	        <view 
	          v-for="(item,index) in leftMenuList"
	          :key="item"
			:class="index==currentIndex ? 'active':''"
			class="menu_item"
	          :data-index="index"
	          @click="handleMenuItemChange"
	        >{{item}}
	        </view>
	      </scroll-view>
	    <!-- 左侧菜单 结束 -->
	
	    <!-- 右侧商品数据 开始 -->
	    <scroll-view scroll-y class="right_content" :scroll-top="scrollTop" @scroll="scroll">
	      <view class="productType"
	      v-for="productType in rightContext"
	      :key="productType.id"
	      >
	      <view class="productType_title">
	        {{productType.name}}
	      </view>
	      <view class="product_list"
	        v-for="product in productType.productList"
	        :key="product.id"
	        >
	        <navigator
			:url="'/pages/product_detail/product_detail?id='+product.id"
			>
	          <image mode="widthFix" :src="apiBaseUrl+'/image/product/'+product.proPic"></image>
	          <view class="right">
	            <view class="product_name">{{product.name}}</view>
	          <view class="product_price"> ¥ {{product.price}}</view>
	          </view>
	        </navigator>
	        </view>
	      </view>
	    </scroll-view>
	    <!-- 右侧商品数据 结束 -->
	  </view>
	</view>
</template>

<script>
	import SearchBar from "@/components/SearchBar.vue";
	import {
		requestUtil,
		getSmallType
	} from "../../utils/request.js";
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				apiBaseUrl:apiBaseUrl,
				currentIndex:0,// 当前选中左侧菜单的索引
				leftMenuList:[],  // 左侧菜单数据
				rightContext:[],
				oldscroll:0,
				scrollTop:0,
				Cates:[], // 右侧商品数据 
			};
		},
		onLoad() {
			getSmallType((res) =>{
				this.Cates=res.message;
				console.log('cate:',res)
				    this.leftMenuList=this.Cates.map(v=>v.name)
				    this.rightContext=this.Cates[this.currentIndex].smallTypeList;
			})
			// setInterval(() =>{
			// 	console.log(this.scrollTop)
			// },1000)
		},
		methods:{
			handleMenuItemChange(e){
			    let {index}=e.currentTarget.dataset;
			    this.rightContext=this.Cates[index].smallTypeList;
			    this.currentIndex = index;
				this.scrollTop = this.oldscroll
				//当视图渲染结束 重新设置为0
				this.$nextTick(() =>{
					this.scrollTop = 0
				});
			},
			scroll: (function(e) {
				 var t=null
				return function(e){
					if(t){
						clearTimeout(t)
					}
					t=setTimeout(() =>{
						this.oldscroll = e.detail.scrollTop
					},0)
				}
				
			})(),
			  // async getCates2(index) {
			  //   const baseUrl = getBaseUrl();
			  //   const result = await requestUtil({
			  //     url: '/bigType/findCategories',
			  //     method: "GET"
			  //   });
			  //   this.Cates=result.message;
			  //   console.log("获取到Cates 02")
			  //   let leftMenuList=this.Cates.map(v=>v.name)
			  //   let rightContext=this.Cates[index].smallTypeList;
			  //   this.setData({
			  //     leftMenuList,
			  //     rightContext,
			  //     currentIndex:index,
			  //     scrollTop:0,
			  //     baseUrl
			  //   })
			  // },
		},
		onReady: function () {
			
		},
		
		  /**
		   * 生命周期函数--监听页面显示
		   */
		onShow: function () {
			const app=getApp();
			    const {index}=app.globalData;
			
			    if(index!=-1){  // 从首页跳转过来
			      // let rightContext=this.Cates[index].smallTypeList;
			      // this.setData({
			      //   leftMenuList,
			      //   rightContext
			      // })
				  this.currentIndex = index
				  this.rightContext=this.Cates[index].smallTypeList;
			      // this.getCates2(index);
			      app.globalData.index=-1; // 重置index
			    }
		},
		
		  /**
		   * 生命周期函数--监听页面隐藏
		   */
		  onHide: function () {
		
		  },
		
		  /**
		   * 生命周期函数--监听页面卸载
		   */
		  onUnload: function () {
		
		  },
		
		  /**
		   * 页面相关事件处理函数--监听用户下拉动作
		   */
		  onPullDownRefresh: function () {
		
		  },
		
		  /**
		   * 页面上拉触底事件的处理函数
		   */
		  onReachBottom: function () {
		
		  },
		
		  /**
		   * 用户点击右上角分享
		   */
		  onShareAppMessage: function () {
		
		  },
		components:{
			SearchBar,
		}
	}
</script>

<style scoped lang="less">
	.cates{
	  height: 100%;
	  .cates_container{
	    height: ~'calc( 100vh - 90rpx )';
	    display: flex;
		flex-wrap: nowrap;
	    .left_menu{
	       flex:2;
	       .menu_item{
	         margin-top: 10rpx;
	         height: 80rpx;
	         display: flex;
	         justify-content: center;
	         align-items: center;
	         font-size: 30rpx;
	       }
	       .active{
	         font-weight: bolder;
	         color: var(--themeColor);
	         border-left: 8rpx solid currentColor;
	       }
	    }
	    .right_content{
	       flex:5;
		   overflow: hidden;
	       .productType{
	         padding: 30rpx 20rpx 30rpx 20rpx;
	        .productType_title{
	          font-weight: bolder;
	        }
	        .product_list{
	         navigator{
	           display: flex;
	           margin: 10rpx;
	           background-color: #F8F8F8;
	           image{
	             flex: 1;
	           }
	           .right{
	             flex: 3;
	             display: flex;
	             flex-direction: column;
	             justify-content: space-around;
	             .product_name{
	                padding-top: 10rpx;
	                white-space: nowrap;
	                overflow: hidden;
	                text-overflow: ellipsis;
	             }
	             .product_price{
	                padding-bottom: 10rpx;
	             }
	           }
	         }
	        }
	       }
	    }
	  }
	}
</style>
