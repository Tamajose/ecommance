-- Tables
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    address_line VARCHAR(255),
    address_city VARCHAR(255),
    address_postal_code VARCHAR(255),
    address_country VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    price NUMERIC(12, 2) NOT NULL,
    stock_quantity INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    seller_username VARCHAR(255) NOT NULL,
    imageurl VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- Inserting Default Users Details
INSERT INTO users (username, email, password, name, phone, role, enabled, created_at) VALUES 
('admin', 'admin@ecommance.com', 'admin123', 'Admin', '+8801234567890', 'ADMIN', true, NOW()),
('seller', 'seller@ecommance.com', 'seller123', 'Seller User', '+8801234567891', 'SELLER', true, NOW()),
('buyer', 'buyer@ecommance.com', 'buyer123', 'Buyer User', '+8801234567892', 'BUYER', true, NOW());

-- Inserting Products Details
INSERT INTO products (name, description, price, stock_quantity, category, seller_username, imageurl, active, created_at, updated_at) VALUES 
('Black and Gray Athletic Cotton Socks - 6 Pairs', 'Keywords: socks, sports, apparel', 1090.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/athletic-cotton-socks-6-pairs.jpg', true, NOW(), NOW()),
('Intermediate Size Basketball', 'Keywords: sports, basketballs', 2095.00, 100, 'SPORTS', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/intermediate-composite-basketball.jpg', true, NOW(), NOW()),
('Adults Plain Cotton T-Shirt - 2 Pack', 'Keywords: tshirts, apparel, mens', 799.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/adults-plain-cotton-tshirt-2-pack-teal.jpg', true, NOW(), NOW()),
('2 Slot Toaster - Black', 'Keywords: toaster, kitchen, appliances', 1899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/2-slot-toaster-white.jpg', true, NOW(), NOW()),
('6 Piece White Dinner Plate Set', 'Keywords: plates, kitchen, dining', 2067.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/elegant-white-dinner-plate-set.jpg', true, NOW(), NOW()),
('6-Piece Nonstick, Carbon Steel Oven Bakeware Baking Set', 'Keywords: kitchen, cookware', 3499.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/3-piece-cooking-set.jpg', true, NOW(), NOW()),
('Plain Hooded Fleece Sweatshirt', 'Keywords: hoodies, sweaters, apparel', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-cozy-fleece-hoodie-light-teal.jpg', true, NOW(), NOW()),
('Luxury Towel Set - Graphite Gray', 'Keywords: bathroom, washroom, restroom, towels, bath towels', 3599.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/luxury-towel-set.jpg', true, NOW(), NOW()),
('Liquid Laundry Detergent, 110 Loads, 82.5 Fl Oz', 'Keywords: bathroom, cleaning', 2899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/laundry-detergent-tabs.jpg', true, NOW(), NOW()),
('Waterproof Knit Athletic Sneakers - Gray', 'Keywords: shoes, running shoes, footwear', 3390.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/knit-athletic-sneakers-gray.jpg', true, NOW(), NOW()),
('Women''s Chiffon Beachwear Cover Up - Black', 'Keywords: robe, swimsuit, swimming, bathing, apparel', 2070.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-striped-beach-dress.jpg', true, NOW(), NOW()),
('Round Sunglasses', 'Keywords: accessories, shades', 1560.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/round-sunglasses-gold.jpg', true, NOW(), NOW()),
('Women''s Two Strap Buckle Sandals - Tan', 'Keywords: footwear, sandals, womens, beach, summer', 2499.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-sandal-heels-white-pink.jpg', true, NOW(), NOW()),
('Blackout Curtains Set 4-Pack - Beige', 'Keywords: bedroom, curtains, home', 4599.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/blackout-curtain-set-beige.jpg', true, NOW(), NOW()),
('Men''s Slim-Fit Summer Shorts', 'Keywords: shorts, apparel, mens', 1699.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-summer-jean-shorts.jpg', true, NOW(), NOW()),
('Electric Glass and Steel Hot Tea Water Kettle - 1.7-Liter', 'Keywords: water boiler, appliances, kitchen', 3074.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/electric-steel-hot-water-kettle-white.jpg', true, NOW(), NOW()),
('Ultra Soft Tissue 2-Ply - 18 Box', 'Keywords: kleenex, tissues, kitchen, tissues box, napkins', 2374.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/facial-tissue-2-ply-8-boxes.jpg', true, NOW(), NOW()),
('Straw Lifeguard Sun Hat', 'Keywords: hats, straw hats, summer, apparel', 2200.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/straw-sunhat.jpg', true, NOW(), NOW()),
('Sterling Silver Sky Flower Stud Earrings', 'Keywords: jewelry, accessories, womens', 1799.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/sky-leaf-branch-earrings.jpg', true, NOW(), NOW()),
('Women''s Stretch Popover Hoodie', 'Keywords: hooded, hoodies, sweaters, womens, apparel', 1374.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-plain-cotton-oversized-sweater-gray.jpg', true, NOW(), NOW()),
('Bathroom Bath Rug Mat 20 x 31 Inch - Grey', 'Keywords: bathmat, bathroom, home', 1250.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/bathroom-mat.jpg', true, NOW(), NOW()),
('Women''s Knit Ballet Flat', 'Keywords: shoes, flats, womens, footwear', 2640.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-knit-ballet-flat-white.jpg', true, NOW(), NOW()),
('Men''s Regular-Fit Quick-Dry Golf Polo Shirt', 'Keywords: tshirts, shirts, apparel, mens', 1599.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-golf-polo-t-shirt-gray.jpg', true, NOW(), NOW()),
('Trash Can with Foot Pedal - Brushed Stainless Steel', 'Keywords: garbage, bins, cans, kitchen', 8300.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/trash-can-with-foot-pedal-50-liter.jpg', true, NOW(), NOW()),
('Duvet Cover Set with Zipper Closure', 'Keywords: bedroom, bed sheets, sheets, covers, home', 2399.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/duvet-cover-set-gray-queen.jpg', true, NOW(), NOW()),
('Women''s Chunky Cable Beanie - Gray', 'Keywords: hats, winter hats, beanies, tuques, apparel, womens', 1250.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-knit-beanie-pom-pom-blue.jpg', true, NOW(), NOW()),
('Men''s Classic-fit Pleated Chino Pants', 'Keywords: pants, apparel, mens', 2290.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-chino-pants-beige.jpg', true, NOW(), NOW()),
('Men''s Athletic Sneaker', 'Keywords: shoes, running shoes, footwear, mens', 3890.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-athletic-shoes-white.jpg', true, NOW(), NOW()),
('Men''s Navigator Sunglasses Pilot', 'Keywords: sunglasses, glasses, accessories, shades', 1690.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-navigator-sunglasses-black.jpg', true, NOW(), NOW()),
('Non-Stick Cookware Set, Pots, Pans and Utensils - 15 Pieces', 'Keywords: cooking set, kitchen', 6797.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/non-stick-cooking-set-4-pieces.jpg', true, NOW(), NOW()),
('Vanity Mirror with Heavy Base - Chrome', 'Keywords: bathroom, washroom, mirrors, home', 1649.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/vanity-mirror-pink.jpg', true, NOW(), NOW()),
('Women''s Fleece Jogger Sweatpant', 'Keywords: pants, sweatpants, jogging, apparel, womens', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-relaxed-lounge-pants-pink.jpg', true, NOW(), NOW()),
('Double Oval Twist French Wire Earrings - Gold', 'Keywords: accessories, womens', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/crystal-zirconia-stud-earrings-pink.jpg', true, NOW(), NOW()),
('Round Airtight Food Storage Containers - 5 Piece', 'Keywords: boxes, food containers, kitchen', 2899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/glass-screw-lid-food-containers.jpg', true, NOW(), NOW()),
('Coffeemaker with Glass Carafe and Reusable Filter - 25 Oz, Black', 'Keywords: coffeemakers, kitchen, appliances', 2250.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/black-and-silver-espresso-maker.jpg', true, NOW(), NOW()),
('Blackout Curtains Set 42 x 84-Inch - Black, 2 Panels', 'Keywords: bedroom, home', 3099.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/blackout-curtains-set-teal.jpg', true, NOW(), NOW()),
('100% Cotton Bath Towels - 2 Pack, Light Teal', 'Keywords: bathroom, home, towels', 2110.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/bath-towel-set-gray-rosewood.jpg', true, NOW(), NOW()),
('Waterproof Knit Athletic Sneakers - Pink', 'Keywords: shoes, running shoes, footwear, womens', 3390.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/knit-athletic-sneakers-pink.webp', true, NOW(), NOW()),
('Countertop Blender - 64oz, 1400 Watts', 'Keywords: food blenders, kitchen, appliances', 10747.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/countertop-push-blender-black.jpg', true, NOW(), NOW()),
('10-Piece Mixing Bowl Set with Lids - Floral', 'Keywords: mixing bowls, baking, cookware, kitchen', 3899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/artistic-bowl-set-6-piece.jpg', true, NOW(), NOW()),
('2-Ply Kitchen Paper Towels - 30 Pack', 'Keywords: kitchen, kitchen towels, tissues', 5799.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/kitchen-paper-towels-8-pack.jpg', true, NOW(), NOW()),
('Men''s Full-Zip Hooded Fleece Sweatshirt', 'Keywords: sweaters, hoodies, apparel, mens', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-stretch-wool-sweater-black.jpg', true, NOW(), NOW()),
('Men''s Athletic Skateboard Shoes - Gray', 'Keywords: shoes, skateboarding, athletic, sneakers, footwear', 3299.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/athletic-skateboard-shoes-gray.jpg', true, NOW(), NOW()),
('Men''s Brown Low-Top Casual Sneakers', 'Keywords: shoes, sneakers, casual, mens, footwear', 2799.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-brown-flat-sneakers.jpg', true, NOW(), NOW());
