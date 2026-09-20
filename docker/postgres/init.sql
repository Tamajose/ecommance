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
('Black and Gray Athletic Cotton Socks - 6 Pairs', 'Breathable cotton crew socks built for everyday sport and gym wear.', 1090.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/athletic-cotton-socks-6-pairs.jpg', true, NOW(), NOW()),
('Intermediate Size Basketball', 'Composite-leather basketball sized for intermediate players and casual games.', 2095.00, 100, 'SPORTS', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/intermediate-composite-basketball.jpg', true, NOW(), NOW()),
('Adults Plain Cotton T-Shirt - 2 Pack', 'Soft plain cotton tees for men, sold as a versatile everyday 2-pack.', 799.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/adults-plain-cotton-tshirt-2-pack-teal.jpg', true, NOW(), NOW()),
('2 Slot Toaster - Black', 'Compact 2-slice toaster with adjustable browning for the kitchen counter.', 1899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/2-slot-toaster-white.jpg', true, NOW(), NOW()),
('6 Piece White Dinner Plate Set', 'Classic white dinnerware set for everyday dining, six plates included.', 2067.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/elegant-white-dinner-plate-set.jpg', true, NOW(), NOW()),
('6-Piece Nonstick, Carbon Steel Oven Bakeware Baking Set', 'Durable nonstick carbon-steel bakeware set for oven baking essentials.', 3499.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/3-piece-cooking-set.jpg', true, NOW(), NOW()),
('Plain Hooded Fleece Sweatshirt', 'Cozy fleece hoodie with a relaxed fit for cooler days.', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-cozy-fleece-hoodie-light-teal.jpg', true, NOW(), NOW()),
('Luxury Towel Set - Graphite Gray', 'Plush, absorbent bath towel set in a rich graphite gray finish.', 3599.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/luxury-towel-set.jpg', true, NOW(), NOW()),
('Liquid Laundry Detergent, 110 Loads, 82.5 Fl Oz', 'High-efficiency liquid laundry detergent good for up to 110 loads.', 2899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/laundry-detergent-tabs.jpg', true, NOW(), NOW()),
('Waterproof Knit Athletic Sneakers - Gray', 'Water-resistant knit sneakers built for running and daily wear.', 3390.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/knit-athletic-sneakers-gray.jpg', true, NOW(), NOW()),
('Women''s Chiffon Beachwear Cover Up - Black', 'Lightweight chiffon cover-up perfect for the beach or poolside.', 2070.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-striped-beach-dress.jpg', true, NOW(), NOW()),
('Round Sunglasses', 'Retro-inspired round sunglasses with a gold-tone frame.', 1560.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/round-sunglasses-gold.jpg', true, NOW(), NOW()),
('Women''s Two Strap Buckle Sandals - Tan', 'Buckled two-strap sandals in tan, ideal for beach and summer outings.', 2499.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-sandal-heels-white-pink.jpg', true, NOW(), NOW()),
('Blackout Curtains Set 4-Pack - Beige', 'Light-blocking blackout curtain panels in beige, sold as a 4-pack.', 4599.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/blackout-curtain-set-beige.jpg', true, NOW(), NOW()),
('Men''s Slim-Fit Summer Shorts', 'Lightweight slim-fit shorts designed for warm-weather comfort.', 1699.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-summer-jean-shorts.jpg', true, NOW(), NOW()),
('Electric Glass and Steel Hot Tea Water Kettle - 1.7-Liter', 'Fast-boiling 1.7-liter electric kettle with a glass and steel body.', 3074.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/electric-steel-hot-water-kettle-white.jpg', true, NOW(), NOW()),
('Ultra Soft Tissue 2-Ply - 18 Box', 'Ultra-soft 2-ply facial tissues, sold in an 18-box household pack.', 2374.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/facial-tissue-2-ply-8-boxes.jpg', true, NOW(), NOW()),
('Straw Lifeguard Sun Hat', 'Wide-brim straw sun hat for summer days outdoors.', 2200.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/straw-sunhat.jpg', true, NOW(), NOW()),
('Sterling Silver Sky Flower Stud Earrings', 'Delicate sterling silver stud earrings with a flower design.', 1799.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/sky-leaf-branch-earrings.jpg', true, NOW(), NOW()),
('Women''s Stretch Popover Hoodie', 'Relaxed stretch popover hoodie for casual, comfortable layering.', 1374.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-plain-cotton-oversized-sweater-gray.jpg', true, NOW(), NOW()),
('Bathroom Bath Rug Mat 20 x 31 Inch - Grey', 'Soft, absorbent bath mat sized 20 x 31 inches in grey.', 1250.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/bathroom-mat.jpg', true, NOW(), NOW()),
('Women''s Knit Ballet Flat', 'Lightweight knit ballet flats offering all-day comfort.', 2640.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-knit-ballet-flat-white.jpg', true, NOW(), NOW()),
('Men''s Regular-Fit Quick-Dry Golf Polo Shirt', 'Quick-dry regular-fit polo shirt suited for golf or casual wear.', 1599.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-golf-polo-t-shirt-gray.jpg', true, NOW(), NOW()),
('Trash Can with Foot Pedal - Brushed Stainless Steel', 'Hands-free foot-pedal trash can with a brushed stainless steel finish.', 8300.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/trash-can-with-foot-pedal-50-liter.jpg', true, NOW(), NOW()),
('Duvet Cover Set with Zipper Closure', 'Easy-care duvet cover set with a secure zipper closure.', 2399.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/duvet-cover-set-gray-queen.jpg', true, NOW(), NOW()),
('Women''s Chunky Cable Beanie - Gray', 'Chunky cable-knit beanie to keep warm through winter.', 1250.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-knit-beanie-pom-pom-blue.jpg', true, NOW(), NOW()),
('Men''s Classic-fit Pleated Chino Pants', 'Classic-fit pleated chinos suited for both work and casual wear.', 2290.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-chino-pants-beige.jpg', true, NOW(), NOW()),
('Men''s Athletic Sneaker', 'Lightweight athletic sneakers built for running and daily training.', 3890.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-athletic-shoes-white.jpg', true, NOW(), NOW()),
('Men''s Navigator Sunglasses Pilot', 'Classic pilot-style navigator sunglasses with UV protection.', 1690.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-navigator-sunglasses-black.jpg', true, NOW(), NOW()),
('Non-Stick Cookware Set, Pots, Pans and Utensils - 15 Pieces', '15-piece nonstick cookware set with pots, pans, and utensils.', 6797.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/non-stick-cooking-set-4-pieces.jpg', true, NOW(), NOW()),
('Vanity Mirror with Heavy Base - Chrome', 'Chrome vanity mirror with a heavy, stable base for the bathroom.', 1649.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/vanity-mirror-pink.jpg', true, NOW(), NOW()),
('Women''s Fleece Jogger Sweatpant', 'Soft fleece jogger sweatpants for lounging or light workouts.', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/women-relaxed-lounge-pants-pink.jpg', true, NOW(), NOW()),
('Double Oval Twist French Wire Earrings - Gold', 'Gold-tone double oval twist earrings with a classic French wire.', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/crystal-zirconia-stud-earrings-pink.jpg', true, NOW(), NOW()),
('Round Airtight Food Storage Containers - 5 Piece', 'Airtight round food storage containers, sold as a 5-piece set.', 2899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/glass-screw-lid-food-containers.jpg', true, NOW(), NOW()),
('Coffeemaker with Glass Carafe and Reusable Filter - 25 Oz, Black', '25-ounce coffeemaker with a glass carafe and reusable filter.', 2250.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/black-and-silver-espresso-maker.jpg', true, NOW(), NOW()),
('Blackout Curtains Set 42 x 84-Inch - Black, 2 Panels', 'Two-panel blackout curtain set measuring 42 x 84 inches in black.', 3099.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/blackout-curtains-set-teal.jpg', true, NOW(), NOW()),
('100% Cotton Bath Towels - 2 Pack, Light Teal', '100% cotton bath towels in light teal, sold as a 2-pack.', 2110.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/bath-towel-set-gray-rosewood.jpg', true, NOW(), NOW()),
('Waterproof Knit Athletic Sneakers - Pink', 'Water-resistant knit sneakers for women in a vibrant pink finish.', 3390.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/knit-athletic-sneakers-pink.webp', true, NOW(), NOW()),
('Countertop Blender - 64oz, 1400 Watts', 'Powerful 1400-watt countertop blender with a 64-ounce jar.', 10747.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/countertop-push-blender-black.jpg', true, NOW(), NOW()),
('10-Piece Mixing Bowl Set with Lids - Floral', 'Floral-patterned mixing bowl set with lids, ten pieces total.', 3899.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/artistic-bowl-set-6-piece.jpg', true, NOW(), NOW()),
('2-Ply Kitchen Paper Towels - 30 Pack', 'Absorbent 2-ply kitchen paper towels, sold in a 30-pack.', 5799.00, 100, 'HOME', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/kitchen-paper-towels-8-pack.jpg', true, NOW(), NOW()),
('Men''s Full-Zip Hooded Fleece Sweatshirt', 'Full-zip fleece hoodie offering warmth for everyday wear.', 2400.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-stretch-wool-sweater-black.jpg', true, NOW(), NOW()),
('Men''s Athletic Skateboard Shoes - Gray', 'Durable athletic skate shoes built for skateboarding and street wear.', 3299.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/athletic-skateboard-shoes-gray.jpg', true, NOW(), NOW()),
('Men''s Brown Low-Top Casual Sneakers', 'Casual low-top sneakers in brown, suited for everyday wear.', 2799.00, 100, 'CLOTHING', 'seller', 'https://res.cloudinary.com/dafe0amdh/image/upload/men-brown-flat-sneakers.jpg', true, NOW(), NOW());
