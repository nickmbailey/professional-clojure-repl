(ns chapter2.core)

(defonce id-atom (atom 0))
(defn next-id [] (swap! id-atom inc))

(defonce tasks (atom (sorted-map)))
(defrecord Task [task creation-time])

(defn now
  "Returns a java.util.Date object representing the current time."
  []
  (new java.util.Date))

(defn get-task-lists
  "Get the names of all created task lists."
  []
  (keys @tasks))

(defn get-tasks
  "Get all tasks on the specified to-do list."
  [list-name]
  (get @tasks list-name))

(defn add-task
  "Add a task to the specified to-do list. Accepts the name of
  the list and a string describing the task."
  [list-name task]
  (letfn [(maybe-add-task [current-list]
            (if (some #(= task (:task (second %1))) current-list)
              current-list
              (assoc current-list (next-id) (Task. task (now)))))]
  (swap! tasks update list-name maybe-add-task)))

(defn remove-list
  "Delete an entire list of tasks at a time. Accepts the name of
  the list to delete."
  [list-name]
  (swap! tasks dissoc list-name))

(defn remove-task
  "Removes a task from the specified to-do list. Accepts the name
  of the list and the id of the task to remove."
  [list-name task-id]
  (swap! tasks update-in [list-name] dissoc task-id))
