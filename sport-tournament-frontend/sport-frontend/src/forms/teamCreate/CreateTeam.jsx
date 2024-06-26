import React, { useState, useEffect } from "react";
import axios from "axios";
import "./CreateTeam.css";

const CreateTeam = () => {
  const [showForm, setShowForm] = useState(false);
  const [hideButton, setHideButton] = useState(true);
  const [categories, setCategories] = useState([]);
  const [errorMessage, setErrorMessage] = useState("");
  const [formData, setFormData] = useState({
    name: "",
    category: "amateur",
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
        console.error("There was an error fetching the categories!", error);
        setErrorMessage("Failed to fetch categories.");
      }
    };

    fetchCategories();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const handleHide = () => {
    setShowForm(true);
    setHideButton(false);
  };

  const handleCancel = () => {
    setShowForm(false);
    setHideButton(true);
    setErrorMessage(""); // Clear any previous error message on cancel
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/teams",
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("Team created:", response.data);
      setShowForm(false);
      setHideButton(true);
      setFormData({ name: "", category: "amateur" });
      setErrorMessage(""); // Clear any previous error message on successful submission
    } catch (error) {
      console.error("There was an error creating the team!", error);
        setErrorMessage(error.response?.data || "An error occurred while creating the team.");
    }
  };

  return (
    <div className="form-create-team">
      {hideButton && <button onClick={handleHide}>Create Team</button>}

      {showForm && (
        <form onSubmit={handleSubmit} className="form-container">
          <div>
            <label htmlFor="name">Name:</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="category">Category:</label>
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleInputChange}
              required
            >
              {categories.map((category) => (
                <option key={category} value={category}>
                  {category}
                </option>
              ))}
            </select>
          </div>
          <div className="buttons">
            <button type="submit">Submit</button>
            <button type="button" onClick={handleCancel}>
              Cancel
            </button>
          </div>
          {errorMessage && (
            <div className="error-message">
              <p>{errorMessage}</p>
            </div>
          )}
        </form>
      )}
    </div>
  );
};

export default CreateTeam;
