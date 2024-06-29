import React, { useState, useEffect } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import "./CreateTournamentForm.css";

const CreateTournamentForm = () => {
  const [categories, setCategories] = useState([]);
  const [status, setStatus] = useState("");
  const [locations, setLocations] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [isOrganizer, setIsOrganizer] = useState(false);
  const [tournamentData, setTournamentData] = useState({
    tournamentName: "",
    sportType: "",
    tournamentCategory: "",
    locationName: "",
    startAt: "",
    endAt: "",
    startHour: 0,
    endHour: 0,
    teamCount: 0,
    matchDuration: 0,
    teamMemberCount: 0,
  });

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decodedToken = jwtDecode(token);
      console.log("token");
      if (
        decodedToken.role === "[ROLE_Organizer]" ||
        decodedToken.role === "[ROLE_Admin]"
      ) {
        setIsOrganizer(true);
      }
    }
  }, []);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          "http://localhost:8080/tournament/categories",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setCategories(response.data);
        console.log(response.data);
      } catch (error) {
        setError(
          error.response?.data || "There was an error fetching the categories!"
        );
        console.error("Error fetching categories:", error);
      }
    };

    const fetchLocations = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/location", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setLocations(response.data);
        console.log(response.data);
      } catch (error) {
        console.error("Error fetching locations:", error);
      }
    };

    fetchCategories();
    fetchLocations();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTournamentData({ ...tournamentData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(tournamentData);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/tournament",
        tournamentData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("Tournament created:", response.data);
      setStatus("Tournament was created");
    } catch (err) {
      console.error("Error creating tournament:", err.response.body);
      setStatus(err.response?.data || "Error when creating tournament");
    }
  };

  return (
    <div className="create-tournament-form-container">
      {!showForm && (
        <>
          <button className="btn-show" onClick={() => setShowForm(true)}>
            Create Tournament
          </button>
        </>
      )}
      {isOrganizer && showForm && (
        <>
          <h2>Create Tournament</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-row">
              <div className="form-group">
                <label>Tournament Name:</label>
                <input
                  type="text"
                  name="tournamentName"
                  value={tournamentData.tournamentName}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Sport Type:</label>
                <input
                  type="text"
                  name="sportType"
                  value={tournamentData.sportType}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Tournament Category:</label>
                <select
                  name="tournamentCategory"
                  value={tournamentData.tournamentCategory}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select a category</option>
                  {categories.map((category) => (
                    <option key={category} value={category}>
                      {category}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Location Name:</label>
                <select
                  id="location"
                  name="locationName"
                  value={tournamentData.locationName}
                  onChange={handleChange}
                >
                  <option value="">Select a location</option>
                  {locations.map((location) => (
                    <option key={location.id} value={location.locationName}>
                      {location.locationName}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Start Date:</label>
                <input
                  type="date"
                  name="startAt"
                  value={tournamentData.startAt}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>End Date:</label>
                <input
                  type="date"
                  name="endAt"
                  value={tournamentData.endAt}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Start Hour:</label>
                <input
                  type="number"
                  name="startHour"
                  value={tournamentData.startHour}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>End Hour:</label>
                <input
                  type="number"
                  name="endHour"
                  value={tournamentData.endHour}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Team Count:</label>
                <input
                  type="number"
                  name="teamCount"
                  value={tournamentData.teamCount}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Match Duration:</label>
                <input
                  type="number"
                  name="matchDuration"
                  value={tournamentData.matchDuration}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Team Member Count:</label>
                <input
                  type="number"
                  name="teamMemberCount"
                  value={tournamentData.teamMemberCount}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
            <button type="submit">Create Tournament</button>
          </form>
          <p>{status}</p>
        </>
      )}
    </div>
  );
};

export default CreateTournamentForm;
