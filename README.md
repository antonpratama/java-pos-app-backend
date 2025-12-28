# POS Backend Service

Backend service untuk aplikasi **Point of Sale (POS)** skala kecil–menengah.  
Project ini menyediakan REST API untuk kebutuhan kasir dan admin, seperti manajemen produk, kategori, transaksi penjualan, dan autentikasi berbasis JWT.

---

## 🚀 Fitur Utama

### 🔐 Authentication & Authorization
- Register user
- Login user
- JWT-based authentication
- Role-based authorization
    - ADMIN
    - CASHIER

---

### 📦 Product Management
- Create, update, delete (soft delete) product
- Get product list (active only)
- Get product by ID
- Search product (pagination, sorting, filtering)
- Product memiliki:
    - name
    - description
    - sku
    - price
    - stock
    - category
    - isActive

---

### 🗂️ Product Category Management
- CRUD product category
- Get product by category
- Soft delete category
- Pagination & sorting

---

### 🖼️ Product Images
- Upload gambar produk (maksimal **3 gambar per produk**)
- Update (replace) semua gambar produk
- Get gambar berdasarkan productId
- Soft delete gambar
- Gambar disimpan langsung di database (`BYTEA / LOB`)
- Response gambar dalam bentuk **Base64**

---

### 🧾 Sales / Transaction
- Create sales transaction
- Generate invoice number otomatis
- Validasi pembayaran:
    - `paidAmount >= totalAmount`
- Hitung otomatis:
    - totalAmount
    - changeAmount
- Get sales:
    - Semua sales aktif
    - By cashier
    - By sale ID
- Soft delete sales

---

## 🧱 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Web
- Spring Security (JWT)
- Spring Data JPA (Hibernate)

### Database
- PostgreSQL

### Security
- JWT Authentication
- Stateless session
- Role-based access control