import React, { useState, useEffect } from 'react';
import { Plus, Package, Calendar, MapPin, Camera, Upload } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { donationService } from '../../services/api';

const DonorDashboard = () => {
  const { user } = useAuth();
  const [showAddForm, setShowAddForm] = useState(false);
  const [myDonations, setMyDonations] = useState([]);
  const [formData, setFormData] = useState({
    foodType: '',
    description: '',
    quantity: '',
    unit: 'kg',
    expiryDate: '',
    pickupLocation: '',
    images: []
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleImageUpload = (e) => {
    const files = Array.from(e.target.files);
    setFormData(prev => ({
      ...prev,
      images: [...prev.images, ...files]
    }));
  };

  const removeImage = (index) => {
    setFormData(prev => ({
      ...prev,
      images: prev.images.filter((_, i) => i !== index)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    console.log('Submit started, user:', user);
    
    if (!user || !user.id) {
      alert('Please log in to add a donation.');
      return;
    }
    
    try {
      // Convert datetime-local format to ISO format
      const expiryDateTime = new Date(formData.expiryDate).toISOString();
      
      // Prepare data according to backend DonationRequest format
      const donationData = {
        donorId: user.id,
        foodType: formData.foodType,
        description: formData.description,
        quantity: parseFloat(formData.quantity),
        unit: formData.unit,
        expiryTime: expiryDateTime,
        pickupLocation: formData.pickupLocation
      };

      console.log('Sending donation data:', donationData);

      const response = await donationService.create(donationData);

      console.log('Response:', response);

      if (response.status === 200 || response.status === 201) {
        alert('Food donation added successfully!');
        setFormData({
          foodType: '',
          description: '',
          quantity: '',
          unit: 'kg',
          expiryDate: '',
          pickupLocation: '',
          images: []
        });
        setShowAddForm(false);
        // Refresh donations list
        loadMyDonations();
      } else {
        throw new Error('Failed to add donation - Status: ' + response.status);
      }
    } catch (error) {
      console.error('Error adding donation:', error);
      console.error('Error details:', error.response);
      const errorMessage = error.response?.data?.message || error.message || 'Unknown error';
      alert('Failed to add donation: ' + errorMessage);
    }
  };

  const loadMyDonations = async () => {
    if (!user || !user.id) return;
    
    try {
      const response = await donationService.getByDonor(user.id);
      setMyDonations(response.data || []);
    } catch (error) {
      console.error('Error loading donations:', error);
    }
  };

  useEffect(() => {
    loadMyDonations();
  }, []);

  return (
    <div className="space-y-6">
      {/* Header with Add Button */}
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Donor Dashboard</h2>
          <p className="text-gray-600">Manage your food donations and help reduce waste</p>
        </div>
        <button
          onClick={() => setShowAddForm(true)}
          className="flex items-center px-4 py-2 rounded-lg font-bold focus:outline-none transition-transform"
          style={{
            backgroundColor: '#16a34a', // green-600
            color: '#ffffff',
            border: '1px solid #065f46',
            boxShadow: '0 4px 10px rgba(16, 163, 127, 0.18)',
            opacity: 1,
            cursor: 'pointer'
          }}
        >
          <Plus className="h-4 w-4 mr-2" />
          Add Food Donation
        </button>
      </div>

      {/* Add Food Form Modal */}
      {showAddForm && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="p-6">
              <div className="flex justify-between items-center mb-6">
                <h3 className="text-lg font-semibold text-gray-900">Add Food Donation</h3>
                <button
                  onClick={() => setShowAddForm(false)}
                  className="text-gray-400 hover:text-gray-600"
                >
                  ×
                </button>
              </div>

              <form onSubmit={handleSubmit} className="space-y-6">
                {/* Food Type */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Food Type *
                  </label>
                  <input
                    type="text"
                    name="foodType"
                    required
                    value={formData.foodType}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                    placeholder="e.g., Fresh Vegetables, Cooked Meals, Packaged Food"
                  />
                </div>

                {/* Description */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Description *
                  </label>
                  <textarea
                    name="description"
                    required
                    value={formData.description}
                    onChange={handleInputChange}
                    rows="3"
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                    placeholder="Describe the food items, condition, and any special instructions"
                  />
                </div>

                {/* Quantity and Unit */}
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Quantity *
                    </label>
                    <input
                      type="number"
                      name="quantity"
                      required
                      step="0.1"
                      min="0"
                      value={formData.quantity}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Unit *
                    </label>
                    <select
                      name="unit"
                      required
                      value={formData.unit}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                    >
                      <option value="kg">Kilograms (kg)</option>
                      <option value="lbs">Pounds (lbs)</option>
                      <option value="pieces">Pieces</option>
                      <option value="portions">Portions</option>
                      <option value="liters">Liters</option>
                    </select>
                  </div>
                </div>

                {/* Expiry Date */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Expiry Date *
                  </label>
                  <input
                    type="datetime-local"
                    name="expiryDate"
                    required
                    min={new Date().toISOString().slice(0, 16)}
                    value={formData.expiryDate}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  />
                </div>

                {/* Pickup Location */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Pickup Location *
                  </label>
                  <textarea
                    name="pickupLocation"
                    required
                    value={formData.pickupLocation}
                    onChange={handleInputChange}
                    rows="2"
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                    placeholder="Full address where food can be picked up"
                  />
                </div>

                {/* Image Upload */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Food Images
                  </label>
                  <div className="border-2 border-dashed border-gray-300 rounded-lg p-4">
                    <div className="text-center">
                      <Camera className="mx-auto h-12 w-12 text-gray-400" />
                      <div className="mt-2">
                        <label className="cursor-pointer">
                          <span className="mt-2 block text-sm font-medium text-gray-900">
                            Upload food images
                          </span>
                          <input
                            type="file"
                            multiple
                            accept="image/*"
                            onChange={handleImageUpload}
                            className="hidden"
                          />
                        </label>
                        <p className="mt-1 text-xs text-gray-500">
                          PNG, JPG, GIF up to 10MB each
                        </p>
                      </div>
                    </div>
                  </div>

                  {/* Image Preview */}
                  {formData.images.length > 0 && (
                    <div className="mt-4 grid grid-cols-2 md:grid-cols-3 gap-4">
                      {formData.images.map((image, index) => (
                        <div key={index} className="relative">
                          <img
                            src={URL.createObjectURL(image)}
                            alt={`Preview ${index + 1}`}
                            className="w-full h-24 object-cover rounded-lg"
                          />
                          <button
                            type="button"
                            onClick={() => removeImage(index)}
                            className="absolute top-1 right-1 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center text-xs hover:bg-red-600"
                          >
                            ×
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                </div>

                {/* Submit Buttons */}
                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => setShowAddForm(false)}
                    className="px-4 py-2 text-gray-700 bg-gray-200 rounded-lg hover:bg-gray-300 transition-colors"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                  >
                    Add Donation
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* My Donations */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-900 flex items-center">
            <Package className="h-5 w-5 mr-2 text-green-600" />
            My Donations
          </h3>
        </div>
        <div className="p-6">
          {myDonations.length > 0 ? (
            <div className="space-y-4">
              {myDonations.map((donation) => (
                <div key={donation.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <h4 className="font-semibold text-gray-900">{donation.foodType}</h4>
                      <p className="text-sm text-gray-600 mt-1">{donation.description}</p>
                      <div className="flex items-center gap-4 mt-2 text-sm text-gray-500">
                        <span className="flex items-center gap-1">
                          <Package className="h-4 w-4" />
                          {donation.quantity} {donation.unit}
                        </span>
                        <span className="flex items-center gap-1">
                          <MapPin className="h-4 w-4" />
                          {donation.pickupLocation}
                        </span>
                        <span className="flex items-center gap-1">
                          <Calendar className="h-4 w-4" />
                          {new Date(donation.expiryTime).toLocaleDateString()}
                        </span>
                      </div>
                    </div>
                    <div className="ml-4">
                      <span className={`px-2 py-1 text-xs rounded-full ${
                        donation.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                        donation.status === 'ACCEPTED' ? 'bg-blue-100 text-blue-800' :
                        donation.status === 'PICKED_UP' ? 'bg-orange-100 text-orange-800' :
                        donation.status === 'DELIVERED' ? 'bg-green-100 text-green-800' :
                        'bg-gray-100 text-gray-800'
                      }`}>
                        {donation.status}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center text-gray-500 py-8">
              <Package className="h-12 w-12 text-gray-300 mx-auto mb-4" />
              <p>No donations yet. Click "Add Food Donation" to get started!</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DonorDashboard;