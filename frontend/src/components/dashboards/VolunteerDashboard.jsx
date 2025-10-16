import React, { useState, useEffect } from 'react';
import { Truck, MapPin, Clock, CheckCircle } from 'lucide-react';

const VolunteerDashboard = () => {
  const [availableDeliveries, setAvailableDeliveries] = useState([]);
  const [myDeliveries, setMyDeliveries] = useState([]);

  const acceptDelivery = (deliveryId) => {
    // TODO: Accept delivery assignment
    console.log('Accepting delivery:', deliveryId);
  };

  const completeDelivery = (deliveryId) => {
    // TODO: Mark delivery as completed
    console.log('Completing delivery:', deliveryId);
  };

  useEffect(() => {
    // TODO: Load available and assigned deliveries
    setAvailableDeliveries([
      {
        id: 1,
        orderId: 'ORD-001',
        pickupLocation: 'Green Bistro, Downtown',
        deliveryLocation: 'Community Center, North District',
        scheduledTime: '2025-09-21T14:00:00',
        foodItems: 'Fresh Vegetables (10 kg)',
        distance: '5.2 km',
        status: 'AVAILABLE'
      }
    ]);
  }, []);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Volunteer Dashboard</h2>
        <p className="text-gray-600">Help deliver food donations to NGOs and communities</p>
      </div>

      {/* Available Deliveries */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-900 flex items-center">
            <Truck className="h-5 w-5 mr-2 text-blue-600" />
            Available Deliveries
          </h3>
        </div>
        <div className="p-6">
          {availableDeliveries.length > 0 ? (
            <div className="space-y-4">
              {availableDeliveries.map((delivery) => (
                <div key={delivery.id} className="border border-gray-200 rounded-lg p-4">
                  <div className="flex justify-between items-start mb-3">
                    <div>
                      <h4 className="font-medium text-gray-900">Order #{delivery.orderId}</h4>
                      <p className="text-sm text-gray-600">{delivery.foodItems}</p>
                    </div>
                    <span className="text-sm text-blue-600 font-medium">{delivery.distance}</span>
                  </div>
                  
                  <div className="space-y-2 mb-4">
                    <div className="flex items-center text-sm text-gray-600">
                      <MapPin className="h-4 w-4 mr-2 text-green-500" />
                      <strong>Pickup:</strong>&nbsp;{delivery.pickupLocation}
                    </div>
                    <div className="flex items-center text-sm text-gray-600">
                      <MapPin className="h-4 w-4 mr-2 text-red-500" />
                      <strong>Delivery:</strong>&nbsp;{delivery.deliveryLocation}
                    </div>
                    <div className="flex items-center text-sm text-gray-600">
                      <Clock className="h-4 w-4 mr-2 text-blue-500" />
                      <strong>Scheduled:</strong>&nbsp;{new Date(delivery.scheduledTime).toLocaleString()}
                    </div>
                  </div>
                  
                  <button
                    onClick={() => acceptDelivery(delivery.id)}
                    className="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    Accept Delivery
                  </button>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center text-gray-500 py-8">
              <Truck className="h-12 w-12 text-gray-300 mx-auto mb-4" />
              <p>No deliveries available at the moment.</p>
            </div>
          )}
        </div>
      </div>

      {/* My Deliveries */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-900 flex items-center">
            <CheckCircle className="h-5 w-5 mr-2 text-green-600" />
            My Deliveries
          </h3>
        </div>
        <div className="p-6">
          <div className="text-center text-gray-500 py-8">
            <CheckCircle className="h-12 w-12 text-gray-300 mx-auto mb-4" />
            <p>No assigned deliveries yet.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default VolunteerDashboard;