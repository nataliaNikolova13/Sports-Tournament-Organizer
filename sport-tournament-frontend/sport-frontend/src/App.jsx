import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import RegistrationForm from "./forms/registration/Registration";
import LoginForm from "./forms/login/Login";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Navbar from "./navbar/Navbar";
import UserProfile from "./profileInfo/user/UserProfile";
import { jwtDecode } from "jwt-decode";

function App() {
  const decodeToken = () => {
    const token = localStorage.getItem("token");
    if (token) {
      const decodedToken = jwtDecode(token);
      return decodedToken;
    }
    return null;
  };

  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/login" element={<LoginForm></LoginForm>}></Route>
        <Route
          path="/registration"
          element={<RegistrationForm></RegistrationForm>}
        ></Route>
        <Route
          path="/profile"
          element={<UserProfile decodeToken={decodeToken}></UserProfile>}
        ></Route>
      </Routes>
    </Router>
  );
}

export default App;
