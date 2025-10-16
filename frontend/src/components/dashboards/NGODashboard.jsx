import React, { useState, useEffect } from 'react';
import { ShoppingCart, MapPin, Calendar, Clock, QrCode, Plus, Minus, Package, Eye } from 'lucide-react';
import { donationService, orderService } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

const NGODashboard = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('browse'); // 'browse' or 'orders'
  const [availableFood, setAvailableFood] = useState([]);
  const [myOrders, setMyOrders] = useState([]);
  const [cart, setCart] = useState([]);
  const [showOrderForm, setShowOrderForm] = useState(false);
  const [orderDetails, setOrderDetails] = useState({
    deliveryLocation: '',
    deliveryDate: '',
    deliveryTime: '',
    specialInstructions: ''
  });
  const [showQRCode, setShowQRCode] = useState(false);
  const [orderConfirmation, setOrderConfirmation] = useState(null);

  const addToCart = (foodItem) => {
    const existingItem = cart.find(item => item.id === foodItem.id);
    if (existingItem) {
      setCart(cart.map(item =>
        item.id === foodItem.id
          ? { ...item, requestedQuantity: item.requestedQuantity + 1 }
          : item
      ));
    } else {
      setCart([...cart, { ...foodItem, requestedQuantity: 1 }]);
    }
  };

  const removeFromCart = (foodId) => {
    setCart(cart.filter(item => item.id !== foodId));
  };

  const updateQuantity = (foodId, newQuantity) => {
    if (newQuantity <= 0) {
      removeFromCart(foodId);
      return;
    }
    setCart(cart.map(item =>
      item.id === foodId
        ? { ...item, requestedQuantity: Math.min(newQuantity, item.quantity) }
        : item
    ));
  };

  const handleOrderSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const orderData = {
        ngoId: user.id,
        items: cart,
        deliveryDetails: orderDetails,
        orderDate: new Date().toISOString()
      };

      // Submit order to backend using the order service
      const response = await orderService.create(orderData);
      
      if (response.data) {
        const result = response.data;
        setOrderConfirmation({
          orderId: result.orderId || 'ORD-' + Date.now(),
          qrCode: result.qrCode || generateQRCodeData(orderData),
          ...orderData
        });
        setShowQRCode(true);
        setCart([]);
        setShowOrderForm(false);
        alert('Order placed successfully!');
        // Reload orders to show the new order
        loadMyOrders();
      } else {
        throw new Error('Failed to place order');
      }
    } catch (error) {
      console.error('Error placing order:', error);
      alert('Failed to place order: ' + (error.response?.data?.message || error.message));
    }
  };

  const generateQRCodeData = (orderData) => {
    return JSON.stringify({
      orderId: 'ORD-' + Date.now(),
      deliveryLocation: orderData.deliveryDetails.deliveryLocation,
      deliveryDate: orderData.deliveryDetails.deliveryDate,
      deliveryTime: orderData.deliveryDetails.deliveryTime,
      items: orderData.items.length,
      timestamp: new Date().toISOString()
    });
  };

  const loadAvailableFood = async () => {
    try {
      // Load available food from API
      const response = await donationService.getAvailable();
      setAvailableFood(response.data);
    } catch (error) {
      console.error('Error loading available food:', error);
      // Show empty list if no real data available
      setAvailableFood([]);
    }
  };

  const loadMyOrders = async () => {
    try {
      if (user && user.id) {
        const response = await orderService.getByNgo(user.id);
        setMyOrders(response.data);
      }
    } catch (error) {
      console.error('Error loading orders:', error);
      setMyOrders([]);
    }
  };

  useEffect(() => {
    loadAvailableFood();
    if (user && user.id) {
      loadMyOrders();
    }
  }, [user]);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">NGO Dashboard</h2>
          <p className="text-gray-600">Browse and request food donations for your community</p>
        </div>
        {cart.length > 0 && activeTab === 'browse' && (
          <div className="flex items-center space-x-4">
            <div className="flex items-center text-gray-600">
              <ShoppingCart className="h-5 w-5 mr-2" />
              Cart ({cart.length} items)
            </div>
            <button
              onClick={() => setShowOrderForm(true)}
              className="px-4 py-2 rounded-lg font-bold focus:outline-none transition-transform"
              style={{
                backgroundColor: '#16a34a',
                color: '#ffffff',
                border: '1px solid #065f46',
                boxShadow: '0 4px 10px rgba(16, 163, 127, 0.18)',
                cursor: 'pointer'
              }}
            >
              Place Order
            </button>
          </div>
        )}
      </div>

      {/* Tab Navigation */}
      <div className="border-b border-gray-200">
        <nav className="-mb-px flex space-x-8">
          <button
            onClick={() => setActiveTab('browse')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'browse'
                ? 'border-blue-500 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            <ShoppingCart className="h-5 w-5 inline mr-2" />
            Browse Food
          </button>
          <button
            onClick={() => setActiveTab('orders')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'orders'
                ? 'border-blue-500 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            <Package className="h-5 w-5 inline mr-2" />
            Your Orders ({myOrders.length})
          </button>
        </nav>
      </div>

      {/* Browse Food Tab */}
      {activeTab === 'browse' && (
        <>
          {/* Available Food Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {availableFood.map((food) => (
          <div key={food.id} className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
            <div className="p-6">
              <div className="flex justify-between items-start mb-4">
                <h3 className="text-lg font-semibold text-gray-900">{food.foodType}</h3>
                <span className="text-sm text-gray-500">#{food.id}</span>
              </div>
              
              <p className="text-gray-600 mb-4">{food.description}</p>
              
              <div className="space-y-2 mb-4">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Quantity:</span>
                  <span className="font-medium">{food.quantity} {food.unit}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Expires:</span>
                  <span className="font-medium text-red-600">
                    {new Date(food.expiryTime).toLocaleDateString()} at {new Date(food.expiryTime).toLocaleTimeString()}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Donor:</span>
                  <span className="font-medium">{food.donorName}</span>
                </div>
              </div>

              <div className="flex items-center mb-4">
                <MapPin className="h-4 w-4 text-gray-400 mr-1" />
                <span className="text-sm text-gray-600">{food.pickupLocation}</span>
              </div>

              <div className="flex justify-between items-center">
                {cart.find(item => item.id === food.id) ? (
                  <div className="flex items-center space-x-2">
                    <button
                      onClick={() => updateQuantity(food.id, cart.find(item => item.id === food.id).requestedQuantity - 1)}
                      className="p-1 text-gray-500 hover:text-gray-700"
                    >
                      <Minus className="h-4 w-4" />
                    </button>
                    <span className="font-medium">
                      {cart.find(item => item.id === food.id).requestedQuantity}
                    </span>
                    <button
                      onClick={() => updateQuantity(food.id, cart.find(item => item.id === food.id).requestedQuantity + 1)}
                      className="p-1 text-gray-500 hover:text-gray-700"
                    >
                      <Plus className="h-4 w-4" />
                    </button>
                    <button
                      onClick={() => removeFromCart(food.id)}
                      className="ml-2 text-red-500 hover:text-red-700 text-sm"
                    >
                      Remove
                    </button>
                  </div>
                ) : (
                  <button
                    onClick={() => addToCart(food)}
                    className="flex items-center px-3 py-1 rounded-lg font-bold text-sm focus:outline-none transition-transform"
                    style={{
                      backgroundColor: '#16a34a',
                      color: '#ffffff',
                      border: '1px solid #065f46',
                      boxShadow: '0 4px 8px rgba(16, 163, 127, 0.12)',
                      cursor: 'pointer'
                    }}
                  >
                    <Plus className="h-4 w-4 mr-1" />
                    Add to Cart
                  </button>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {availableFood.length === 0 && (
        <div className="text-center text-gray-500 py-12">
          <ShoppingCart className="h-12 w-12 text-gray-300 mx-auto mb-4" />
          <p>No food donations available at the moment.</p>
        </div>
      )}
        </>
      )}

      {/* Your Orders Tab */}
      {activeTab === 'orders' && (
        <div className="space-y-4">
          {myOrders.length > 0 ? (
            myOrders.map((order) => (
              <div key={order.id} className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900">Order #{order.orderId}</h3>
                    <p className="text-sm text-gray-500">
                      Placed on {new Date(order.createdAt).toLocaleDateString()} at {new Date(order.createdAt).toLocaleTimeString()}
                    </p>
                  </div>
                  <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                    order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                    order.status === 'CONFIRMED' ? 'bg-blue-100 text-blue-800' :
                    order.status === 'IN_TRANSIT' ? 'bg-purple-100 text-purple-800' :
                    order.status === 'DELIVERED' ? 'bg-green-100 text-green-800' :
                    'bg-red-100 text-red-800'
                  }`}>
                    {order.status}
                  </span>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                  <div>
                    <h4 className="font-medium text-gray-900 mb-2">Delivery Details</h4>
                    <div className="space-y-1 text-sm text-gray-600">
                      <div className="flex items-start">
                        <MapPin className="h-4 w-4 text-gray-400 mt-0.5 mr-2 flex-shrink-0" />
                        <span>{order.deliveryLocation}</span>
                      </div>
                      <div className="flex items-center">
                        <Calendar className="h-4 w-4 text-gray-400 mr-2" />
                        <span>{new Date(order.deliveryDate).toLocaleDateString()}</span>
                      </div>
                      <div className="flex items-center">
                        <Clock className="h-4 w-4 text-gray-400 mr-2" />
                        <span>{order.deliveryTime}</span>
                      </div>
                    </div>
                  </div>

                  <div>
                    <h4 className="font-medium text-gray-900 mb-2">Order Items</h4>
                    <div className="space-y-1 text-sm text-gray-600">
                      {order.orderItems && order.orderItems.map((item, index) => (
                        <div key={index} className="flex justify-between">
                          <span>{item.donation?.foodType || 'Food Item'}</span>
                          <span>{item.requestedQuantity} {item.unit}</span>
                        </div>
                      ))}
                      {(!order.orderItems || order.orderItems.length === 0) && (
                        <span className="text-gray-400">Items loading...</span>
                      )}
                    </div>
                  </div>
                </div>

                {order.specialInstructions && (
                  <div className="mb-4">
                    <h4 className="font-medium text-gray-900 mb-1">Special Instructions</h4>
                    <p className="text-sm text-gray-600">{order.specialInstructions}</p>
                  </div>
                )}

                <div className="flex justify-between items-center pt-4 border-t border-gray-100">
                  <div className="flex items-center text-sm text-gray-500">
                    <QrCode className="h-4 w-4 mr-1" />
                    <span>Order ID: {order.orderId}</span>
                  </div>
                  <button
                    onClick={() => {
                      setOrderConfirmation({
                        orderId: order.orderId,
                        qrCode: order.qrCode,
                        deliveryDetails: {
                          deliveryLocation: order.deliveryLocation,
                          deliveryDate: new Date(order.deliveryDate).toLocaleDateString(),
                          deliveryTime: order.deliveryTime
                        }
                      });
                      setShowQRCode(true);
                    }}
                    className="flex items-center px-3 py-1 text-blue-600 hover:text-blue-700 transition-colors"
                  >
                    <Eye className="h-4 w-4 mr-1" />
                    View QR Code
                  </button>
                </div>
              </div>
            ))
          ) : (
            <div className="text-center text-gray-500 py-12">
              <Package className="h-12 w-12 text-gray-300 mx-auto mb-4" />
              <p>No orders placed yet.</p>
              <p className="text-sm mt-2">Start browsing food donations to place your first order!</p>
              <button
                onClick={() => setActiveTab('browse')}
                className="mt-4 px-4 py-2 rounded-lg font-bold focus:outline-none transition-transform"
                style={{
                  backgroundColor: '#16a34a',
                  color: '#ffffff',
                  border: '1px solid #065f46',
                  boxShadow: '0 4px 10px rgba(16, 163, 127, 0.18)',
                  cursor: 'pointer'
                }}
              >
                Browse Food
              </button>
            </div>
          )}
        </div>
      )}

      {/* Order Form Modal */}
      {showOrderForm && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="p-6">
              <div className="flex justify-between items-center mb-6">
                <h3 className="text-lg font-semibold text-gray-900">Place Order</h3>
                <button
                  onClick={() => setShowOrderForm(false)}
                  className="text-gray-400 hover:text-gray-600"
                >
                  ×
                </button>
              </div>

              <form onSubmit={handleOrderSubmit} className="space-y-6">
                {/* Order Summary */}
                <div>
                  <h4 className="font-medium text-gray-900 mb-3">Order Summary</h4>
                  <div className="space-y-2">
                    {cart.map((item) => (
                      <div key={item.id} className="flex justify-between text-sm">
                        <span>{item.foodType}</span>
                        <span>{item.requestedQuantity} {item.unit}</span>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Delivery Location */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Delivery Location *
                  </label>
                  <textarea
                    required
                    value={orderDetails.deliveryLocation}
                    onChange={(e) => setOrderDetails({...orderDetails, deliveryLocation: e.target.value})}
                    rows="3"
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Full address where food should be delivered"
                  />
                </div>

                {/* Delivery Date */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Preferred Delivery Date *
                  </label>
                  <input
                    type="date"
                    required
                    value={orderDetails.deliveryDate}
                    onChange={(e) => setOrderDetails({...orderDetails, deliveryDate: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                {/* Delivery Time */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Preferred Delivery Time *
                  </label>
                  <input
                    type="time"
                    required
                    value={orderDetails.deliveryTime}
                    onChange={(e) => setOrderDetails({...orderDetails, deliveryTime: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                {/* Special Instructions */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Special Instructions
                  </label>
                  <textarea
                    value={orderDetails.specialInstructions}
                    onChange={(e) => setOrderDetails({...orderDetails, specialInstructions: e.target.value})}
                    rows="2"
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Any special delivery instructions or requirements"
                  />
                </div>

                {/* Submit Buttons */}
                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => setShowOrderForm(false)}
                    className="px-4 py-2 text-gray-700 bg-gray-200 rounded-lg hover:bg-gray-300 transition-colors"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    Place Order
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* QR Code Modal */}
      {showQRCode && orderConfirmation && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-md w-full">
            <div className="p-6 text-center">
              <div className="mb-6">
                <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <QrCode className="h-8 w-8 text-green-600" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900 mb-2">Order Confirmed!</h3>
                <p className="text-gray-600">Order ID: {orderConfirmation.orderId}</p>
              </div>

              {/* QR Code Placeholder */}
              <div className="bg-gray-100 p-6 rounded-lg mb-6">
                <div className="w-32 h-32 bg-gray-300 mx-auto rounded flex items-center justify-center">
                  <QrCode className="h-16 w-16 text-gray-400" />
                </div>
                <p className="text-xs text-gray-500 mt-2">QR Code for order tracking</p>
              </div>

              {/* Order Details */}
              <div className="text-left text-sm space-y-2 mb-6">
                <div className="flex justify-between">
                  <span className="text-gray-500">Delivery Location:</span>
                  <span className="font-medium">{orderConfirmation.deliveryDetails.deliveryLocation}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Date:</span>
                  <span className="font-medium">{orderConfirmation.deliveryDetails.deliveryDate}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Time:</span>
                  <span className="font-medium">{orderConfirmation.deliveryDetails.deliveryTime}</span>
                </div>
              </div>

              <button
                onClick={() => setShowQRCode(false)}
                className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default NGODashboard;