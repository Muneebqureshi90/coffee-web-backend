package com.example.coffee.controllers;

import com.example.coffee.dto.ProductDto;
import com.example.coffee.services.FileService;
import com.example.coffee.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/product")
@Tag(name = "Product Controller", description = "This is Product Controller")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;
    @PostMapping("/{categoryId}")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, @PathVariable("categoryId") Integer categoryId) {
        try {
            ProductDto productDto1 = this.productService.createProduct(productDto, categoryId);
            return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//@PostMapping("/{categoryId}")
//public ResponseEntity<ProductDto> createProduct(
//        @RequestPart("productDto") ProductDto productDto,
//        @RequestPart("image") MultipartFile image,
//        @PathVariable("categoryId") Integer categoryId
//) {
//    try {
//        productDto.setImage(image);
//        ProductDto createdProduct = this.productService.createProduct(productDto, categoryId);
//        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
//    } catch (Exception e) {
//        e.printStackTrace();
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}



    @PutMapping("/{pId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("pId") Integer pId, @RequestBody ProductDto productDto) {
        try {
            ProductDto productDto1 = this.productService.updateProduct(pId, productDto);
            return new ResponseEntity<>(productDto1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        try {
            List<ProductDto> productDto1 = this.productService.getAllProducts();
            return new ResponseEntity<>(productDto1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Integer id) {
        try {
            ProductDto productDto1 = this.productService.getProductById(id);
            return new ResponseEntity<>(productDto1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/add-multiple")
    public List<ProductDto> addMultipleProducts(@RequestBody List<ProductDto> productDtos) {
        return productService.addMultipleProducts(productDtos);
    }
    @PostMapping("/image/upload/{productId}")
    public ResponseEntity<ProductDto> uploadPostImage(
            @RequestParam("image") MultipartFile file,
            @PathVariable Integer productId
    ) throws IOException {
        // Get the product by ID
        ProductDto productById = this.productService.getProductById(productId);

        // Upload the image and get the original file name
        String originalFileName = this.fileService.uploadImage(path, file);

        // Set the image URL in the product DTO
        productById.setImageUrl(originalFileName);

        // Update the product with the new image URL
        ProductDto updatedProduct = this.productService.updateProduct(productId, productById);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
