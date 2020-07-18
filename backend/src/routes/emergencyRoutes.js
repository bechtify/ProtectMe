const router = require("express").Router();
const emergencyController = require("../controllers/emergencyController");
//onst authCheck = require("../middlewares/authCheck");

// GET

// PUT

// POST
//Post an emergency for a specific user --> {JSON-Object}
router.post("/:userId", emergencyController.postEmergency);

// DELETE

module.exports = router;
