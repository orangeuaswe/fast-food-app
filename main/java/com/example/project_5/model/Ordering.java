package com.example.project_5.model;
import com.example.project_5.model.StoredOrder;
import java.util.ArrayList;

public final class Ordering
{
    private static Ordering instance = new Ordering();
    private Order current;
    private StoredOrder storedOrder;
    private Ordering()
    {
        storedOrder = new StoredOrder();
        current = null;
    }
    public static Ordering getInstance()
    {
        return instance;
    }
    public StoredOrder getStoredOrder()
    {
        return storedOrder;
    }
    public Order createNew()
    {
        current = storedOrder.createNewOrder();
        return current;
    }
    public Order getCurrent()
    {
        return current;
    }
    public void setCurrent(Order order)
    {
        this.current = order;
    }
    public void finalize()
    {
        if(current!=null)
        {
            storedOrder.addOrder(current);
            current = null;
        }
    }
    public void cancel()
    {
        current = null;
    }


}
