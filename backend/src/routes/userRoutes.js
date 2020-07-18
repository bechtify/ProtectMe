const router = require("express").Router();
const userController = require("../controllers/userController");
//onst authCheck = require("../middlewares/authCheck");

// GET
// Get user information
router.get("/:userId", userController.getUser);

// PUT

// POST
router.post("/login", userController.logInUser);
router.post("/register", userController.registerUser);
// DELETE

module.exports = router;