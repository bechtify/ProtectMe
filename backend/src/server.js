const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");

const emergencyRoutes = require("./routes/emergencyRoutes");
const userRoutes = require("./routes/userRoutes");
const contactRoutes = require("./routes/contactRoutes");
const env = require("./config/env");

const server = express();

// Enable CORS
server.use(cors());

// Using bodyParser to parse JSON bodies into JS objects ==> https://github.com/expressjs/body-parser
server.use(bodyParser.urlencoded({ extended: false }));
server.use(bodyParser.json());

// Set all routes to /router/{specific}Routes.js
server.use("/emergencies", emergencyRoutes);
server.use("/users", userRoutes);
server.use("/contacts", contactRoutes);

server.listen(env.port, () => {
  console.log("Server started on Port "+env.port);
});