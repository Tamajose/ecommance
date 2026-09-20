import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Products from "./pages/Products";
import ProductDetail from "./pages/ProductDetail";
import Cart from "./pages/Cart";
import Orders from "./pages/Orders";
import MyProducts from "./pages/MyProducts";
import ProductForm from "./pages/ProductForm";
import AdminDashboard from "./pages/AdminDashboard";
import SellerOrders from "./pages/SellerOrders";
import { AuthProvider } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";
import GoogleCallback from "./pages/GoogleCallback";
import "./App.css";

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <BrowserRouter>
          <Navbar />
          <div className="container">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/login/callback" element={<GoogleCallback />} />
              <Route path="/products" element={<Products />} />
              <Route path="/product/:id" element={<ProductDetail />} />
              <Route
                path="/cart"
                element={
                  <ProtectedRoute>
                    <Cart />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/orders"
                element={
                  <ProtectedRoute>
                    <Orders />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/my-products"
                element={
                  <ProtectedRoute roles={["SELLER", "ADMIN"]}>
                    <MyProducts />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/my-products/new"
                element={
                  <ProtectedRoute roles={["SELLER", "ADMIN"]}>
                    <ProductForm />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/my-products/:id/edit"
                element={
                  <ProtectedRoute roles={["SELLER", "ADMIN"]}>
                    <ProductForm />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/outgoing-orders"
                element={
                  <ProtectedRoute roles={["SELLER", "ADMIN"]}>
                    <SellerOrders />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/admin"
                element={
                  <ProtectedRoute roles={["ADMIN"]}>
                    <AdminDashboard />
                  </ProtectedRoute>
                }
              />
            </Routes>
          </div>
        </BrowserRouter>
      </CartProvider>
    </AuthProvider>
  );
}

export default App;