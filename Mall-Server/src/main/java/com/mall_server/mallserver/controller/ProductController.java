package com.mall_server.mallserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall_server.mallserver.entity.Product;
import com.mall_server.mallserver.entity.ProductSwiperImage;
import com.mall_server.mallserver.entity.R;
import com.mall_server.mallserver.service.IProductService;
import com.mall_server.mallserver.service.IProductSwiperImageService;
import com.mall_server.mallserver.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品Controller
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductSwiperImageService productSwiperImageService;

    @Value("${productSwiperImagesFilePath}")
    private String productSwiperImagesFilePath;


    @Value("${productImagesFilePath}")
    private String productImagesFilePath;
    /**
     * 查询轮播商品
     * @return
     */
    @GetMapping("/findSwiper")
    public R findSwiper(){
        List<Product> swiperProductList = productService.list(new QueryWrapper<Product>().eq("isSwiper", true).orderByAsc("swiperSort"));
        Map<String,Object> map=new HashMap<>();
        map.put("message",swiperProductList);
        return R.ok(map);
    }
    /**
     * 查询热门推荐商品8个
     * @return
     */
    @GetMapping("/findHot")
    public R findHot(){
        Page<Product> page=new Page<>(0,8);
        Page<Product> pageProduct = productService.page(page, new QueryWrapper<Product>().eq("isHot", true).orderByAsc("hotDateTime"));
        List<Product> hotProductList = pageProduct.getRecords();
        Map<String,Object> map=new HashMap<>();
        map.put("message",hotProductList);
        return R.ok(map);
    }
    /**
     * 根据id查询商品信息
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public R detail(Integer id){
        Product product = productService.getById(id);
        List<ProductSwiperImage> productSwiperImageList = productSwiperImageService.list(new QueryWrapper<ProductSwiperImage>().eq("productId", product.getId()).orderByAsc("sort"));
        product.setProductSwiperImageList(productSwiperImageList);
        Map<String,Object> map=new HashMap<>();
        map.put("message",product);
        return R.ok(map);
    }

    /**
     * 商品搜索
     * @param q
     * @return
     */
    @GetMapping("/search")
    public R search(String q){
        List<Product> productList = productService.list(new QueryWrapper<Product>().like("name", q));
        Map<String,Object> map=new HashMap<>();
        map.put("message",productList);
        return R.ok(map);
    }
    /**
     * 发布商品
     * @param product
     * @return
     */
    @RequestMapping("/save")
    public R save(@RequestBody Product product){
        if(product.getId()==null || product.getId()==-1){
            productService.add(product);
        }else{
            productService.update(product);
        }
        Map<String,Object> s = new HashMap<>();
        s.put("id",productService.findEnd(1));
        return R.ok(s);
    }

    /**
     * 上传商品图片
     * @param file
     * @return
     * @throws
     */
    @RequestMapping("/uploadImage")
    public String _uploadImage(MultipartFile file){
        Map<String,Object> resultMap=new HashMap<>();
        if(!file.isEmpty()){
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName=DateUtil.getCurrentDateStr()+suffixName;
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(),new File(productImagesFilePath+newFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            dataMap.put("src","/image/product/"+newFileName);
            resultMap.put("data",dataMap);
            return  newFileName;
        }
        return "error";
    }

    /**
     * 上传商品轮播图片
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/productSwiperImage/uploadImage")
    public String uploadImage(MultipartFile file){
        Map<String,Object> resultMap=new HashMap<>();
        if(!file.isEmpty()){
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(),new File(productSwiperImagesFilePath+newFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultMap.put("code",200);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            dataMap.put("src","/image/productSwiperImgs/"+newFileName);
            resultMap.put("data",dataMap);
            return newFileName;
        }
        return "error";
    }
    /**
     * 添加图片
     * @param list
     * @return
     */
    @RequestMapping("/productSwiperImage/add")
    public R add(@RequestBody ArrayList<ProductSwiperImage> list){
        for(int i=0;i<list.size();i++){
            ProductSwiperImage p = list.get(i);
            productSwiperImageService.saveOrUpdate(p);
        }
//        productSwiperImageService.saveOrUpdate(productSwiperImage);
        return R.ok();
    }

}
