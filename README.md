# Food Waste Management System

A comprehensive web application built with Java Spring Boot backend and React frontend to reduce food waste by connecting donors, NGOs, and volunteers.

## 🌟 Features

### For Donors
- **Food Donation Form** - Easy-to-use form to donate surplus food
- **Track Donations** - Monitor donation status from pickup to delivery
- **Impact Dashboard** - See your contribution to reducing food waste
- **Gamification** - Earn points and badges for donations

### For NGOs/Charities
- **Request Food** - Create requests for specific food types and quantities
- **Inventory Management** - Track received donations and distributions
- **Analytics** - View impact metrics and beneficiary data
- **Priority Requests** - Mark urgent food needs

### For Volunteers
- **Delivery Tasks** - Get assigned pickup and delivery missions
- **Route Optimization** - Efficient delivery routes using maps
- **Status Updates** - Real-time updates on delivery progress
- **Community Impact** - Track your contribution to the cause

### For Administrators
- **System Overview** - Complete platform analytics and metrics
- **User Management** - Manage donors, NGOs, and volunteers
- **Report Generation** - Comprehensive impact reports
- **System Configuration** - Platform settings and monitoring

## 🛠 Tech Stack

### Backend
- **Java 17** with Spring Boot 3.2
- **Spring Security** for authentication
- **Spring Data JPA** with Hibernate
- **H2 Database** (development) / MySQL (production)
- **Maven** for dependency management
- **JWT** for stateless authentication

### Frontend
- **React 18** with Vite
- **Tailwind CSS** for styling
- **React Router** for navigation
- **Axios** for API calls
- **Lucide React** for icons
- **Context API** for state management

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- npm or yarn

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   
   The backend will start on `http://localhost:8080`

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```
   
   The frontend will start on `http://localhost:3000`

## 📊 Key Statistics

The system tracks and displays:
- **Food Saved** - Total kilograms of food rescued
- **Meals Provided** - Estimated meals created from donations  
- **People Served** - Number of beneficiaries reached
- **Active Users** - Registered donors, NGOs, and volunteers
- **Carbon Footprint** - Environmental impact reduction

## 🎯 Impact Goals

- **Reduce Food Waste** - Divert surplus food from landfills
- **Fight Hunger** - Connect food with those who need it most
- **Build Community** - Create networks of giving and support
- **Environmental Protection** - Reduce greenhouse gas emissions
- **Awareness** - Educate about food waste and sustainability

## 🔧 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/profile/{id}` - Get user profile

### Donations
- `POST /api/donations` - Create new donation
- `GET /api/donations` - List all donations
- `GET /api/donations/donor/{id}` - Get donations by donor
- `PUT /api/donations/{id}/pickup` - Mark as picked up
- `PUT /api/donations/{id}/deliver` - Mark as delivered

### Food Requests
- `POST /api/requests` - Create food request
- `GET /api/requests/active` - Get active requests
- `PUT /api/requests/{id}/fulfill` - Mark request as fulfilled

### Statistics
- `GET /api/stats/dashboard` - Dashboard statistics
- `GET /api/stats/impact` - Impact metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- React team for the amazing framework
- Tailwind CSS for beautiful styling utilities
- All contributors and volunteers making this project possible

---

**Together, we can end food waste and hunger in our communities! 🌍💚**