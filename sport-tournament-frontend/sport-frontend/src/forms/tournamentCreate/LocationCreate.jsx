import React, { useState, useEffect } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import "./LocationCreate.css";

const CreateLocationForm = () => {
  const [showForm, setShowForm] = useState(false);
  const [isOrganizer, setIsOrganizer] = useState(false);
  const [status, setStatus] = useState("");
  const [locationData, setLocationData] = useState({
    locationName: "",
    venueCount: 0,
  });

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decodedToken = jwtDecode(token);
      console.log("token");
      if (decodedToken.role === "[ROLE_Admin]") {
        setIsOrganizer(true);
      }
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLocationData({ ...locationData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(locationData);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/location",
        locationData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("Location created:", response.data);
      setStatus("Location was created");
    } catch (err) {
      console.error("Error creating location:", err);
      setStatus("Error when creating location");
    }
  };

  return (
    <div className="create-location-form-container">
      {!showForm && (
        <button className="btn-show" onClick={() => setShowForm(true)}>
          Create location
        </button>
      )}
      {isOrganizer && showForm && (
        <>
          <h2>Create Location</h2>
          <form onSubmit={handleSubmit} className="create-location-form">
            <div className="form-group">
              <label htmlFor="locationName">Location Name</label>
              <input
                type="text"
                id="locationName"
                name="locationName"
                value={locationData.locationName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="venueCount">Venue Count</label>
              <input
                type="number"
                id="venueCount"
                name="venueCount"
                value={locationData.venueCount}
                onChange={handleChange}
                required
              />
            </div>
            <button type="submit" className="btn-submit">
              Create Location
            </button>
          </form>
          <p>{status}</p>
        </>
      )}
    </div>
  );
};

export default CreateLocationForm;
