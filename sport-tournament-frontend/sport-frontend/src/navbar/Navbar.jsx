import React, { useState } from "react";
import "./Navbar.css";
import { Link } from "react-router-dom";

const Navbar = () => {
  const [loggedIn, setLoggedIn] = useState(localStorage.getItem("token"));
  const handleLogout = () => {
    localStorage.removeItem("token");
    setLoggedIn(false);
  };
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
              <button className="logout" onClick={handleLogout}>
                Logout
              </button>
            </li>
          </>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;
