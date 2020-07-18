const router = require("express").Router();
const contactController = require("../controllers/contactController");
//onst authCheck = require("../middlewares/authCheck");

// GET
//Get one contact with contact_id
router.get("/:contactId", contactController.getContact);
//Get ALL contact from a specific user
router.get("/user/:userId", contactController.getContacts);

// PUT
//Update a specific contact --> {JSON-Object}
router.put("/:contactId", contactController.editContact);

// POST
//Post new contact for specific user --> {JSON-Object}
router.post("/:userId", contactController.addContact);

// DELETE
//Delete contact for a specific user --> [contact_ids]
router.delete("/:userId", contactController.deleteContacts);

module.exports = router;