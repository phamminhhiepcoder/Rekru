# 🛒 E-Commerce Platform for Fashion Store 

An AI-powered learning platform that helps students study smarter by analyzing uploaded materials, generating summaries, quizzes, and enabling real-time voice interaction with AI.

---

## 🚀 Features

### FashionStore E-commerce Website
- Admin Management: Manage products, categories, orders, inventory, and customer information
- Product Search: Search and filter clothing products by name, category, size, and color
- Shopping Cart: Add, update, and remove products from the cart
- Checkout & Payment: Process orders with Cash on Delivery (COD) and bank transfer via SePay

### 🤖 Groq API Integration
- AI-powered product and inventory queries by size, color, and available quantity
- Context-aware product search based on descriptions and categories
- Natural language queries for product availability and stock information
- AI-assisted product information retrieval and customer support
### ☁ Media & Email Management
- Media files handling via Cloudinary
- Email notifications using Google Email Service:
  - Account registration
  - Reset passwords
  - Order information sending

### 💳 Payment Integration (SePay)
- Secure payment gateway
- Transaction verification handling

---

## 🏗 System Architecture

```
User
 ├── Browse Products
 │      ├── Search Products
 │      └── View Product Details
 │
 ├── Shopping Cart
 │      ├── Add Products to Cart
 │      ├── Update Product Quantity
 │      └── Remove Products from Cart
 │
 └── Checkout
        ├── Enter Shipping Information
        ├── Select Payment Method
        ├── COD Payment
        ├── Bank Transfer via SePay
        └── Place Order

Admin
 ├── Product Management
 │      ├── Add Products
 │      ├── Update Products
 │      └── Delete Products
 │
 ├── Category Management
 │
 ├── Inventory Management
 │      └── Manage Product Variants
 │             ├── Size
 │             ├── Color
 │             └── Stock Quantity
 │
 ├── Order Management
 │      ├── View Orders
 │      ├── Update Order Status
 │      └── Manage Payments
 │
 └── Customer Management
        └── View Customer Information
```

---

## 🛠 Tech Stack

### Backend
- Spring Boot
- RESTful APIs

### AI Services
- Grok API

### Database
- MySQL

### External Services
- Github (File Management)
- Google Email Service

---

## ⚙ Installation

```bash
# Clone repository
git clone https://github.com/phamminhhiepcoder/FashionStore.git

# Run project
Click Run Java in VS Code or Intellij IDEA
```

---

## 🔐 Environment Variables

Setup your keys in application.properties:

```
#Github Upload configuration
cloudinary.cloud_name=<your-cloud-name>
cloudinary.api_key=<your-api-key>
cloudinary.api_secret=<your-api-secret>

#Send email
spring.mail.username=<your-email>
spring.mail.password=<your-password>
#GROK
grok.api.key=
grok.api.url=
grok.model=
```

---

## Screenshots
<img width="1341" height="924" alt="image" src="https://github.com/user-attachments/assets/d448e383-41c7-4119-96eb-431ef635c7b4" />
<img width="1357" height="834" alt="image" src="https://github.com/user-attachments/assets/eada87bd-e3bd-4698-b6e7-6f14c1e573f5" />
<img width="1368" height="850" alt="image" src="https://github.com/user-attachments/assets/c2a6b5c7-5733-46f6-8f6f-5c6651a8790b" />
<img width="1141" height="766" alt="image" src="https://github.com/user-attachments/assets/fa8ef974-f0d0-4bff-ad36-0086a119a04a" />


---

## 📌 Future Improvements
- Multi-language support
- Mobile app integration
- Responsive UI Optimization

