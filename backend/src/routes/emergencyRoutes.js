const router = require("express").Router();
const userController = require("../controllers/emergencyController");
//onst authCheck = require("../middlewares/authCheck");

// GET

// PUT

// POST
//Post an emergency for a specific user --> {JSON-Object}
router.post("/:userId", userController.postEmergency);

// DELETE

module.exports = router;