import React from 'react';
import { useAuth } from '../context/AuthContext';
import DonorDashboard from '../components/dashboards/DonorDashboard';
import NGODashboard from '../components/dashboards/NGODashboard';
import VolunteerDashboard from '../components/dashboards/VolunteerDashboard';

const Dashboard = () => {
  const { user } = useAuth();

  const renderDashboard = () => {
    switch (user?.role) {
      case 'DONOR':
        return <DonorDashboard />;
      case 'NGO':
        return <NGODashboard />;
      case 'VOLUNTEER':
        return <VolunteerDashboard />;
      default:
        return (
          <div className="flex items-center justify-center min-h-[50vh]">
            <div className="text-center">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Welcome to Food Management</h2>
              <p className="text-gray-600">Your dashboard will be available soon.</p>
            </div>
          </div>
        );
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">
            Welcome back, {user?.name || user?.username}!
          </h1>
          <p className="text-gray-600 mt-2">
            Role: <span className="font-medium capitalize">{user?.role?.toLowerCase()}</span>
          </p>
        </div>
        
        {renderDashboard()}
      </div>
    </div>
  );
};

export default Dashboard;