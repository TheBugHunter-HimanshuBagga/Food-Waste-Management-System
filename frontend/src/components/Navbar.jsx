import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  Heart, 
  Menu, 
  X, 
  User, 
  LogOut, 
  Home,
  Gift,
  HandHeart,
  Users,
  BarChart3,
  BookOpen
} from 'lucide-react';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <nav className="bg-white shadow-lg border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Logo and Brand (About Me button placed next to brand) */}
          <div className="flex items-center">
            <Link to="/" className="flex items-center space-x-2">
              <Heart className="h-8 w-8 text-primary-600" />
              <span className="text-xl font-bold text-gray-900">SUSTAINABLE FOOD WASTE MANAGEMENT</span>
            </Link>
            <a
              href="https://about-me-zeta-weld.vercel.app/"
              target="_blank"
              rel="noopener noreferrer"
              className="ml-8 inline-flex items-center px-2 py-1 rounded-md text-sm font-medium"
              style={{
                backgroundColor: '#16a34a',
                color: '#ffffff',
                border: '1px solid #065f46',
                textDecoration: 'none'
              }}
            >
            </a>
          </div>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center space-x-6">
            <Link to="/" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
              <Home className="h-4 w-4" />
              <span>Home</span>
            </Link>
            
            <Link to="/about" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
              <BookOpen className="h-4 w-4" />
              <span>About</span>
            </Link>

            {isAuthenticated() ? (
              <>
                {user.role === 'DONOR' && (
                  <>
                    <Link to="/donate" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
                      <Gift className="h-4 w-4" />
                      <span>Donate Food</span>
                    </Link>
                    <Link to="/dashboard" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
                      <BarChart3 className="h-4 w-4" />
                      <span>Dashboard</span>
                    </Link>
                  </>
                )}

                {user.role === 'NGO' && (
                  <>
                    <Link to="/request" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
                      <HandHeart className="h-4 w-4" />
                      <span>Request Food</span>
                    </Link>
                    <Link to="/dashboard" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
                      <BarChart3 className="h-4 w-4" />
                      <span>Dashboard</span>
                    </Link>
                  </>
                )}

                {user.role === 'VOLUNTEER' && (
                  <Link to="/dashboard" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
                    <Users className="h-4 w-4" />
                    <span>Deliveries</span>
                  </Link>
                )}

                {user.role === 'ADMIN' && (
                  <Link to="/admin" className="flex items-center space-x-1 text-gray-600 hover:text-primary-600 transition-colors">
                    <BarChart3 className="h-4 w-4" />
                    <span>Admin Panel</span>
                  </Link>
                )}

                {/* User Menu */}
                <div className="flex items-center space-x-3">
                  <span className="text-sm text-gray-600">
                    Hello, {user.name}
                  </span>
                  <div className="flex items-center space-x-2">
                    <User className="h-5 w-5 text-gray-400" />
                    <span className="text-sm font-medium text-primary-600">{user.points} pts</span>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="flex items-center space-x-1 text-gray-600 hover:text-red-600 transition-colors"
                  >
                    <LogOut className="h-4 w-4" />
                    <span>Logout</span>
                  </button>
                </div>
              </>
            ) : (
              <div className="flex items-center space-x-4">
                <Link to="/login" className="text-gray-600 hover:text-primary-600 transition-colors">
                  Login
                </Link>
                <Link to="/register" className="btn-primary">
                  Get Started
                </Link>
              </div>
            )}
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden flex items-center">
            <button
              onClick={toggleMenu}
              className="text-gray-600 hover:text-primary-600 transition-colors"
            >
              {isMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
            </button>
          </div>
        </div>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="md:hidden border-t border-gray-200 py-4">
            <div className="flex flex-col space-y-4">
              <Link 
                to="/" 
                className="text-gray-600 hover:text-primary-600 transition-colors"
                onClick={() => setIsMenuOpen(false)}
              >
                Home
              </Link>
              <Link 
                to="/about" 
                className="text-gray-600 hover:text-primary-600 transition-colors"
                onClick={() => setIsMenuOpen(false)}
              >
                About
              </Link>
              <a
                href="https://about-me-zeta-weld.vercel.app/"
                target="_blank"
                rel="noopener noreferrer"
                className="text-gray-600 hover:text-primary-600 transition-colors"
                onClick={() => setIsMenuOpen(false)}
              >
                
              </a>

              {isAuthenticated() ? (
                <>
                  {user.role === 'DONOR' && (
                    <>
                      <Link 
                        to="/donate" 
                        className="text-gray-600 hover:text-primary-600 transition-colors"
                        onClick={() => setIsMenuOpen(false)}
                      >
                        Donate Food
                      </Link>
                      <Link 
                        to="/dashboard" 
                        className="text-gray-600 hover:text-primary-600 transition-colors"
                        onClick={() => setIsMenuOpen(false)}
                      >
                        Dashboard
                      </Link>
                    </>
                  )}

                  {user.role === 'NGO' && (
                    <>
                      <Link 
                        to="/request" 
                        className="text-gray-600 hover:text-primary-600 transition-colors"
                        onClick={() => setIsMenuOpen(false)}
                      >
                        Request Food
                      </Link>
                      <Link 
                        to="/dashboard" 
                        className="text-gray-600 hover:text-primary-600 transition-colors"
                        onClick={() => setIsMenuOpen(false)}
                      >
                        Dashboard
                      </Link>
                    </>
                  )}

                  <div className="pt-4 border-t border-gray-200">
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-gray-600">
                        Hello, {user.name}
                      </span>
                      <span className="text-sm font-medium text-primary-600">
                        {user.points} pts
                      </span>
                    </div>
                    <button
                      onClick={() => {
                        handleLogout();
                        setIsMenuOpen(false);
                      }}
                      className="mt-2 text-red-600 hover:text-red-700 transition-colors"
                    >
                      Logout
                    </button>
                  </div>
                </>
              ) : (
                <div className="flex flex-col space-y-2 pt-4 border-t border-gray-200">
                  <Link 
                    to="/login" 
                    className="text-gray-600 hover:text-primary-600 transition-colors"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Login
                  </Link>
                  <Link 
                    to="/register" 
                    className="btn-primary w-fit"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Get Started
                  </Link>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;