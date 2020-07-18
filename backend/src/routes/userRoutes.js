const router = require("express").Router();
const userController = require("../controllers/userController");
//onst authCheck = require("../middlewares/authCheck");
//const upload = require("./../middlewares/multer");

// GET
//router.get("/login", authCheck, userController.getMe);
router.get("/:userId", userController.getUsers);
//router.get("/:userId", authCheck, userController.getUsers);

// PUT
// We use multer as a middleware here to parse incoming multipart/form-data
//router.put("/me", authCheck, upload.single("image"), userController.updateMe);

// POST
//router.post("/rating", authCheck, userController.postRating);

// DELETE

module.exports = router;