import { createContext, useContext, useState, useEffect } from "react";
import { getCart as getCartAPI, addItem as addItemAPI, updateItemQuantity as updateItemQuantityAPI,
    removeItem as removeItemAPI, clearCart as clearCartAPI } from "../api/cartApi";

const cartContext = createContext(null);

export function cartProvider({ children }){
    const [ cart, setCart ] = useState(null);
    const { isAuthenticated } = useAuth();

    useEffect(() => {
        if(isAuthenticated){
            fetchCart();
        } else{
            setCart(null);
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
            setCart(response)
        } catch(error){
            console.error("Error adding item to Cart, ", error);
        }
    }

    async function updateQuantity(productId, quantity){
        try{
            const response = await updateItemQuantityAPI(productId, quantity);
            setCart(response)
        } catch(error){
            console.error("Error updating quantity, ", error);
        }
    }

    async function removeItem(productId){
        try{
            const response = await removeItem(productId);
            setCart(response)
        } catch(error){
            console.error("Error removing item to Cart, ", error);
        }
    }

    async function clearCart(){
        try{
            await clearCart();
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