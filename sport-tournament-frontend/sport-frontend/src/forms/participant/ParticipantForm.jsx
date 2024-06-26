import React, { useState, useEffect } from "react";
import axios from "axios";
import "./ParticipantForm.css";

const ParticipantForm = ({ teamId, onClose }) => {
  const [usersAdd, setUsersAdd] = useState([]);
  const [usersDelete, setUsersDelete] = useState([]);
  const [searchEmailAdd, setSearchEmailAdd] = useState("");
  const [searchEmailDelete, setSearchEmailDelete] = useState("");
  const [selectedUserAdd, setSelectedUserAdd] = useState("");
  const [selectedUserDelete, setSelectedUserDelete] = useState("");
  const [error, setError] = useState(null);

  const fetchUserByEmail = async (email, setUsers) => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `http://localhost:8080/users/email/${email}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setUsers([response.data]);
    } catch (error) {
      setError(error.response?.data || "Error fetching user by email. Please try again later.");
      setUsers([]);
    }
  };

  useEffect(() => {
    if (searchEmailAdd) {
      fetchUserByEmail(searchEmailAdd, setUsersAdd);
    } else {
      setUsersAdd([]);
    }
  }, [searchEmailAdd]);

  useEffect(() => {
    if (searchEmailDelete) {
      fetchUserByEmail(searchEmailDelete, setUsersDelete);
    } else {
      setUsersDelete([]);
    }
  }, [searchEmailDelete]);

  const handleSearchChangeAdd = (event) => {
    setSearchEmailAdd(event.target.value);
  };

  const handleSearchChangeDelete = (event) => {
    setSearchEmailDelete(event.target.value);
  };

  const handleSubmitAdd = async (event) => {
    event.preventDefault();
    try {
      const token = localStorage.getItem("token");
      await axios.post(
        "http://localhost:8080/participant",
        {
          teamId: teamId,
          userId: selectedUserAdd,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      onClose();
    } catch (errorAdd) {
      setError(error.response?.data || "Error adding participant. Please try again later.");
    }
  };

  const handleSubmitDelete = async (event) => {
    event.preventDefault();
    try {
      const token = localStorage.getItem("token");
      await axios.delete("http://localhost:8080/participant", {
        data: {
          teamId: teamId,
          userId: selectedUserDelete,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      onClose();
    } catch (error) {
      setError(error.response?.data || "Error deleting participant. Please try again later.");
    }
  };

  return (
    <div className="participant-form">
      <h2>Add Participant to Team</h2>
      {error && <p className="error">{error}</p>}
      <input
        type="text"
        placeholder="Search by email"
        value={searchEmailAdd}
        onChange={handleSearchChangeAdd}
      />
      <form onSubmit={handleSubmitAdd}>
        <button type="submit">Add Participant</button>
      </form>
      <h2>Delete Participant from Team</h2>
      {error && <p className="error">{error}</p>}
      <input
        type="text"
        placeholder="Search by email"
        value={searchEmailDelete}
        onChange={handleSearchChangeDelete}
      />
      <form onSubmit={handleSubmitDelete}>
        <button type="submit">Delete Participant</button>
      </form>
    </div>
  );
};

export default ParticipantForm;
