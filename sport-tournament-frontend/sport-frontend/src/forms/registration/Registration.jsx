import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Registration.css";
import { Link, useNavigate } from "react-router-dom";

const RegistrationForm = () => {
  const initialFormState = {
    fullName: "",
    email: "",
    password: "",
    role: "Participant",
    birthdate: "",
  };
  const [formData, setFormData] = useState(initialFormState);
  const [isLogged, setIsLogged] = useState(false);

  const [errors, setErrors] = useState({});
  const [registrationError, setRegistrationError] = useState("");
  const [redirectToHome, setRedirectToHome] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      setIsLogged(true);
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const validate = () => {
    const errors = {};
    if (!formData.fullName) errors.name = "Name is required";
    if (!formData.email) {
      errors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = "Email address is invalid";
    }
    if (!formData.password) errors.password = "Password is required";
    if (!formData.birthdate) errors.birthdate = "Birthday is required";
    return errors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      try {
        const response = await axios.post(
          "http://localhost:8080/auth/signup",
          formData
        );
        console.log("Form Data Submitted", response.data);
        localStorage.setItem("token", response.data.token);
        setRegistrationError(true);
        clear();
        setIsLogged(true);
        navigate("/");
      } catch (error) {
            const errorMessage = error.response?.data || "An error occurred during registration.";
            setRegistrationError(errorMessage);
      }
    }
  };

  const clear = () => {
    setFormData(initialFormState);
    setErrors({});
    setRegistrationError("");
  };

  return (
    <div className="container">
      {isLogged ? (
        <h2>Welcome to Sports Tournament</h2>
      ) : (
        <>
          <h2>Registration Form</h2>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Name:</label>
              <input
                type="text"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
              />
              {errors.name && <p className="error">{errors.name}</p>}
            </div>
            <div>
              <label>Email:</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
              />
              {errors.email && <p className="error">{errors.email}</p>}
            </div>
            <div>
              <label>Password:</label>
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
              {errors.password && <p className="error">{errors.password}</p>}
            </div>
            <div>
              <label>Birthdate:</label>
              <input
                type="date"
                name="birthdate"
                value={formData.birthdate}
                onChange={handleChange}
              />
              {errors.birthdate && <p className="error">{errors.birthdate}</p>}
            </div>
            <button type="submit">Register</button>
            {registrationError && <p className="error">{registrationError}</p>}
          </form>
        </>
      )}
    </div>
  );
};

export default RegistrationForm;
