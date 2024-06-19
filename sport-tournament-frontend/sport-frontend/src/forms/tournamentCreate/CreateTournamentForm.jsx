import React, { useState, useEffect } from "react";
import axios from "axios";
import "./CreateTournamentForm.css";

const CreateTournamentForm = () => {
  const [categories, setCategories] = useState([]);
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
      } catch (error) {
        setError("There was an error fetching the categories!");
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
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
    } catch (err) {
      console.error("Error creating tournament:", err);
    }
  };

  return (
    <div className="create-tournament-form-container">
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
            <input
              type="text"
              name="locationName"
              value={tournamentData.locationName}
              onChange={handleChange}
              required
            />
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
            <label>Match Duration (minutes):</label>
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
    </div>
  );
};

export default CreateTournamentForm;
