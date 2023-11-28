package com.vzkz.fitjournal.core.boilerplate

import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.Exercises
import com.vzkz.fitjournal.domain.model.SetXrepXweight
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel

val USERMODELFORTESTS: UserModel = UserModel(
    uid = "mUxyPt1uC4hOMcB6jRCcr3upTZN2",
    nickname = "vzkz_88",
    email = "jaimevzkz1+5@gmail.com",
    firstname = "Jaime",
    lastname = "Vázquez",
    weight = 75,
    age = 21,
    gender = "Male",
    goal = "Bulking",
    workouts = listOf(
        WorkoutModel(
            wotName = "Upper body strength",
            duration = 89,
            exCount = 3,
            wotOrder = 1,
            exercises = listOf(
                Exercises(
                    rest = 60,
                    exData = ExerciseModel(
                        exName = "Squat",
                        muscle = "Legs",
                        difficulty = "Begginer",
                        instructions = "Example instruction"
                    ),
                    setXrepXweight = listOf(
                        SetXrepXweight(exNum = "1"),
                        SetXrepXweight(exNum = "2"),
                        SetXrepXweight(exNum = "3")
                    ),
                    setNum = 3,
                    exOrder = 1
                ),
                Exercises(
                    rest = 30,
                    exData = ExerciseModel(
                        exName = "Bench Press",
                        muscle = "Chest",
                        difficulty = "Intermediate",
                        instructions = "Example instruction 2"
                    ),
                    setXrepXweight = listOf(
                        SetXrepXweight(exNum = "1"),
                        SetXrepXweight(exNum = "2"),
                        SetXrepXweight(exNum = "3"),
                        SetXrepXweight(exNum = "4")
                    ),
                    setNum = 4,
                    exOrder = 2
                ),
                Exercises(
                    rest = 60,
                    exData = ExerciseModel(
                        exName = "Biceps curl",
                        muscle = "Biceps",
                        difficulty = "Intermediate",
                        instructions = "Example instruction 3"
                    ),
                    setXrepXweight = listOf(
                        SetXrepXweight(exNum = "1"),
                        SetXrepXweight(exNum = "2"),
                        SetXrepXweight(exNum = "3"),
                        SetXrepXweight(exNum = "4")
                    ),
                    setNum = 4,
                    exOrder = 3
                )
            )
        ),
        WorkoutModel(
            wotName = "Legs strength",
            duration = 105,
            exCount = 4,
            wotOrder = 2,
            exercises = listOf(
                Exercises(
                    rest = 120,
                    exData = ExerciseModel(
                        exName = "Squat",
                        muscle = "Legs",
                        difficulty = "Begginer",
                        instructions = "Example instruction"
                    ),
                    setXrepXweight = listOf(
                        SetXrepXweight(exNum = "1"),
                        SetXrepXweight(exNum = "2"),
                        SetXrepXweight(exNum = "3")
                    ),
                    setNum = 3,
                    exOrder = 1
                ),
                Exercises(
                    rest = 50,
                    exData = ExerciseModel(
                        exName = "Bench Press",
                        muscle = "Chest",
                        difficulty = "Intermediate",
                        instructions = "Example instruction 2"
                    ),
                    setXrepXweight = listOf(
                        SetXrepXweight(exNum = "1"),
                        SetXrepXweight(exNum = "2"),
                        SetXrepXweight(exNum = "3"),
                        SetXrepXweight(exNum = "4")
                    ),
                    setNum = 4,
                    exOrder = 2
                ),
                Exercises(
                    rest = 50,
                    exData = ExerciseModel(
                        exName = "Biceps curl",
                        muscle = "Biceps",
                        difficulty = "Intermediate",
                        instructions = "Example instruction 3"
                    ),
                    setXrepXweight = listOf(
                        SetXrepXweight(exNum = "1"),
                        SetXrepXweight(exNum = "2"),
                        SetXrepXweight(exNum = "3"),
                        SetXrepXweight(exNum = "4")
                    ),
                    setNum = 4,
                    exOrder = 3
                ),
                Exercises(
                    rest = 50,
                    exData = ExerciseModel(
                        exName = "Press",
                        muscle = "Chest",
                        difficulty = "Intermediate",
                        instructions = "Example instruction 2"
                    ),
                    setXrepXweight = listOf(
                        SetXrepXweight(exNum = "1")
                    ),
                    setNum = 1,
                    exOrder = 4
                )
            )
        )
    )
)

class Trash {
}