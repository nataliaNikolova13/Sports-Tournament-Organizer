import React, { useState, useEffect } from "react";
import "./Navbar.css";
import { Link, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const Navbar = () => {
  const [loggedIn, setLoggedIn] = useState(false);
  const navigate = useNavigate();
  const [isAdmin, setIsAdmin] = useState(false);

  const handleLogout = () => {
    localStorage.removeItem("token");
    setLoggedIn(false);
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      setLoggedIn(true);
      navigate("/");
    } else {
      setLoggedIn(false);
    }
  }, [localStorage.getItem("token")]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decoded = jwtDecode(token);
      if (decoded.role === "[ROLE_Admin]") {
        setIsAdmin(true);
      }
    }
  }, [localStorage.getItem("token")]);

  return (
    <nav className="navbar">
      <ul className="navbar-nav">
        <li className="nav-item">
          <Link to="/home">Home</Link>
        </li>
        {!loggedIn && (
          <li className="nav-item">
            <Link to="/login">Login</Link>
          </li>
        )}
        {loggedIn && (
          <>
            <li className="nav-item">
              <Link to="/profile">User Profile</Link>
            </li>
            <li className="nav-item">
              <Link to="/team">My Teams</Link>
            </li>
            <li className="nav-item">
              <Link to="/tournaments">Tournaments</Link>
            </li>
            <li className="nav-item">
              <button className="logout" onClick={handleLogout}>
                Logout
              </button>
            </li>
          </>
        )}
        {isAdmin && (
          <li className="nav-item">
            <Link to="/admin">Admin</Link>
          </li>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;
