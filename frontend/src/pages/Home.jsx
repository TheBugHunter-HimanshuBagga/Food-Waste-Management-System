import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  Heart, 
  Users, 
  Truck, 
  Gift,
  HandHeart,
  BarChart3,
  ArrowRight,
  CheckCircle,
  Star,
  Target
} from 'lucide-react';
import { statsService } from '../services/api';

const Home = () => {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [dashboardResponse, impactResponse] = await Promise.all([
          statsService.getDashboardStats(),
          statsService.getImpactStats()
        ]);
        setStats({
          ...dashboardResponse.data,
          ...impactResponse.data
        });
      } catch (error) {
        console.error('Error fetching stats:', error);
        // Show zeros if no real data is available
        setStats({
          totalDonors: 0,
          totalNgos: 0,
          totalVolunteers: 0,
          foodSavedKg: 0,
          mealsProvided: 0,
          peopleServed: 0,
          successfulDeliveries: 0
        });
      }
    };

    fetchStats();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <section style={{ 
        background: 'linear-gradient(90deg, #16a34a 0%, #15803d 100%)', 
        color: 'white', 
        padding: '5rem 0' 
      }}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="relative">
            {/* About Me removed from hero — link is in the top navbar */}
          
          <div className="text-center">
            <h1 className="hero-title">
              Reducing Food Waste,<br />
              <span style={{ color: '#fbbf24' }}>Feeding Communities</span>
            </h1>
            <p className="hero-subtitle">
              Connect with local NGOs and volunteers to donate surplus food, 
              request meals for those in need, and make a real impact in your community.
            </p>
            <div style={{ 
              display: 'flex', 
              flexDirection: 'column', 
              gap: '1rem', 
              justifyContent: 'center', 
              alignItems: 'center' 
            }} className="sm:flex-row">
              <Link to="/register" className="btn btn-secondary" style={{ 
                fontSize: '1.125rem', 
                padding: '0.75rem 2rem',
                display: 'inline-flex',
                alignItems: 'center',
                backgroundColor: '#eab308',
                color: 'white',
                textDecoration: 'none'
              }}>
                Get Started Today
                <ArrowRight style={{ marginLeft: '0.5rem', width: '1.25rem', height: '1.25rem' }} />
              </Link>
              <Link to="/about" className="btn btn-outline" style={{ 
                fontSize: '1.125rem', 
                padding: '0.75rem 2rem',
                border: '2px solid white',
                color: 'white',
                backgroundColor: 'transparent',
                textDecoration: 'none'
              }}>
                Learn More
              </Link>
            </div>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      {stats && (
        <section className="py-16 bg-white">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="text-center mb-12">
              <h2 className="section-title">Our Impact</h2>
              <p className="text-gray-600 text-lg">Making a difference, one meal at a time</p>
            </div>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
              <div className="stat-card">
                <div className="stat-number">{stats.foodSavedKg?.toLocaleString() || '0'}</div>
                <div className="stat-label">KG Food Saved</div>
              </div>
              <div className="stat-card">
                <div className="stat-number">{stats.mealsProvided?.toLocaleString() || '0'}</div>
                <div className="stat-label">Meals Provided</div>
              </div>
              <div className="stat-card">
                <div className="stat-number">{stats.peopleServed?.toLocaleString() || '0'}</div>
                <div className="stat-label">People Served</div>
              </div>
              <div className="stat-card">
                <div className="stat-number">{stats.totalDonors?.toLocaleString() || '0'}</div>
                <div className="stat-label">Active Donors</div>
              </div>
            </div>
          </div>
        </section>
      )}

      {/* How It Works Section */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="section-title">How It Works</h2>
            <p className="text-gray-600 text-lg">Simple steps to make a big difference</p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Step 1 */}
            <div className="text-center">
              <div className="bg-primary-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                <Gift className="h-8 w-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-3">1. Donate Food</h3>
              <p className="text-gray-600">
                Restaurants, stores, and individuals can easily donate surplus food 
                through our platform with just a few clicks.
              </p>
            </div>

            {/* Step 2 */}
            <div className="text-center">
              <div className="bg-secondary-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                <HandHeart className="h-8 w-8 text-secondary-600" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-3">2. Match & Connect</h3>
              <p className="text-gray-600">
                Our system automatically matches donations with nearby NGOs and 
                food banks that need them most.
              </p>
            </div>

            {/* Step 3 */}
            <div className="text-center">
              <div className="bg-green-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                <Truck className="h-8 w-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-3">3. Deliver Impact</h3>
              <p className="text-gray-600">
                Volunteers pick up and deliver food to those in need, 
                creating real community impact.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="text-center mb-12">
              <h2 className="section-title">Why Choose Food Management?</h2>
            </div>          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <div className="card p-6">
              <CheckCircle className="h-8 w-8 text-primary-600 mb-4" />
              <h3 className="text-xl font-semibold text-gray-900 mb-3">Real-Time Tracking</h3>
              <p className="text-gray-600">
                Track your donations from pickup to delivery with live updates 
                and impact metrics.
              </p>
            </div>

            <div className="card p-6">
              <Users className="h-8 w-8 text-primary-600 mb-4" />
              <h3 className="text-xl font-semibold text-gray-900 mb-3">Verified Partners</h3>
              <p className="text-gray-600">
                All NGOs and volunteers are verified to ensure safe and 
                reliable food distribution.
              </p>
            </div>

            <div className="card p-6">
              <Target className="h-8 w-8 text-primary-600 mb-4" />
              <h3 className="text-xl font-semibold text-gray-900 mb-3">Smart Matching</h3>
              <p className="text-gray-600">
                AI-powered matching ensures food reaches the right people 
                at the right time.
              </p>
            </div>

            <div className="card p-6">
              <Star className="h-8 w-8 text-primary-600 mb-4" />
              <h3 className="text-xl font-semibold text-gray-900 mb-3">Gamification</h3>
              <p className="text-gray-600">
                Earn points and badges for your contributions, making 
                giving back fun and engaging.
              </p>
            </div>

            <div className="card p-6">
              <BarChart3 className="h-8 w-8 text-primary-600 mb-4" />
              <h3 className="text-xl font-semibold text-gray-900 mb-3">Impact Analytics</h3>
              <p className="text-gray-600">
                See your environmental and social impact with detailed 
                reports and statistics.
              </p>
            </div>

            <div className="card p-6">
              <Heart className="h-8 w-8 text-primary-600 mb-4" />
              <h3 className="text-xl font-semibold text-gray-900 mb-3">Community Driven</h3>
              <p className="text-gray-600">
                Join a community of like-minded individuals working 
                together to end food waste.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 bg-primary-600 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl font-bold mb-4">Ready to Make a Difference?</h2>
          <p className="text-xl text-primary-100 mb-8 max-w-2xl mx-auto">
            Join thousands of donors, NGOs, and volunteers who are already making 
            an impact in their communities.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link to="/register?role=donor" className="btn-secondary text-lg px-8 py-3">
              Start Donating
            </Link>
            <Link to="/register?role=ngo" className="btn-outline text-lg px-8 py-3 border-white text-white hover:bg-white hover:text-primary-600 transition-colors">
              Join as NGO
            </Link>
            <Link to="/register?role=volunteer" className="btn-outline text-lg px-8 py-3 border-white text-white hover:bg-white hover:text-primary-600 transition-colors">
              Volunteer
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;