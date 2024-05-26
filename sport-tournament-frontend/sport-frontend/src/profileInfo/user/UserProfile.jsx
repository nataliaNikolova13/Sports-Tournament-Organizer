import React, { useState, useEffect } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import "./UserProfile.css";

const UserProfile = ({ decodeToken }) => {
  const initialState = {
    fullName: "",
    email: "",
    id: "",
    role: "Participant",
  };
  const [user, setUser] = useState(initialState);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const decodedToken = decodeToken();
        if (decodedToken) {
          const id = decodedToken.userid;
          const token = localStorage.getItem("token");
          const response = await axios.get(`http://localhost:8080/user/${id}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          setUser(response.data);
        }
      } catch (error) {
        setError(error.message);
      }
    };

    fetchUser();
  }, []);

  return (
    <div className="user-profile-container">
      <div className="user-profile-header">
        <h2>User Profile</h2>
      </div>
      <div className="user-profile-detail">
        <label>Name:</label>
        <p>{user.name}</p>
      </div>
      <div className="user-profile-detail">
        <label>Email:</label>
        <p>{user.email}</p>
      </div>
      <div className="user-profile-detail">
        <label>Role:</label>
        <p>{user.role}</p>
      </div>
    </div>
  );
};

export default UserProfile;
