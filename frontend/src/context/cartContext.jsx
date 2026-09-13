import { createContext, useState, useEffect } from "react";
import { getCart as getCartAPI, addItem as addItemAPI, updateItemQuantity as updateItemQuantityAPI, removeItem as removeItemAPI, clearCart as clearCartAPI } from '../api/cartApi';
import { useAuth } from './AuthContext';

const cartContext = createContext(null);

export function CartProvider({ children }){
    const [ cart, setCart ] = useState(null);
    const { isAuthenticated } = useAuth();

    useEffect(() => {
    if(isAuthenticated){
        fetchCart();
    }
}, [isAuthenticated]);

    async function fetchCart(){
        try{
            const response = await getCartAPI();
            setCart(response);
        } catch(error){
            console.error("Error fetching Cart, ", error);
        }
    }

    async function addItem(productId, quantity=1){
        try{
            const response = await addItemAPI(productId, quantity);
            setCart(response);
        } catch(error){
            console.error("Error adding item to Cart, ", error);
        }
    }

    async function updateQuantity(productId, quantity){
        try{
            const response = await updateItemQuantityAPI(productId, quantity);
            setCart(response);
        } catch(error){
            console.error("Error updating quantity, ", error);
        }
    }

    async function removeItem(productId){
        try{
            const response = await removeItemAPI(productId);
            setCart(response);
        } catch(error){
            console.error("Error removing item from Cart, ", error);
        }
    }

    async function clearCart(){
        try{
            await clearCartAPI();
            setCart(null);
        } catch(error){
            console.error("Error clearing Cart, ", error);
        }
    }

    return(
        <cartContext.Provider value={{ cart, fetchCart, addItem, updateQuantity, removeItem, clearCart }}>
            {children}
        </cartContext.Provider>
    );
}