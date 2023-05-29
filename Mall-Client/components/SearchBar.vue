<template>
  <view class="search_bar">
	  <navigator url="/pages/Search/Search" open-type="navigate">
		  
	  <icon type="search" size="15"></icon>搜索
	  </navigator>
    
  </view>
</template>

<script>
export default {
  name: "Search",
  data() {
    return {};
  },
  methods: {
    inToSearch() {
		// 去search页面
      uni.navigateTo({ url: ".../pages/Search/Search" });
    },
  },
};
</script>

<style scoped lang="less">
.search_bar {
	  background-color:var(--themeColor);
	  height: 90rpx;
	  padding: 10rpx;
	navigator{
	  height: 100%;
	  display: flex;
	  justify-content: center;
	  align-items: center;
	  background-color: white;
	  color: #666;
	  border-radius: 15rpx;
	  icon{
		  padding: 5rpx;
	  }
	}
	
}
</style>