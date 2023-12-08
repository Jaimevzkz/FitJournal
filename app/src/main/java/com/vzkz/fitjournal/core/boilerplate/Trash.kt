package com.vzkz.fitjournal.core.boilerplate

import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.Exercises
import com.vzkz.fitjournal.domain.model.SetXrepXweight
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import java.time.LocalDate

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
    wotDates = listOf(
        Pair(LocalDate.of(2022, 11, 15), "wZSTSkzOQN25oEKDeocg"),
        Pair(LocalDate.of(2020, 10, 1), "rduoIqR4EJVN4FiEvucp"),
        Pair(LocalDate.of(2023, 4, 1), "rduoIqR4EJVN4FiEvucp"),
        Pair(LocalDate.of(2023, 12, 31), "wZSTSkzOQN25oEKDeocg"),
        Pair(LocalDate.of(2023, 12, 5), "rduoIqR4EJVN4FiEvucp"),
    ),
    workouts = listOf(
        WorkoutModel(
            wid = "wZSTSkzOQN25oEKDeocg",
            wotName = "Upper body strength",
            duration = 89,
            exCount = 3,
            wotOrder = 1,
            exercises = listOf(
                Exercises(
                    exid = "vaK3TB32xdKgMifrKCV3",
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
                    exid = "DIsDh0yIFJ5j5XWfOW81",
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
                    exid = "HpAkRh7jcECHRawrtqjI",
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
            wid = "rduoIqR4EJVN4FiEvucp",
            wotName = "Legs strength",
            duration = 105,
            exCount = 4,
            wotOrder = 2,
            exercises = listOf(
                Exercises(
                    exid = "QOYW1RvcU0aVJnB1aiNa",
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
                    exid = "DIsDh0yIFJ5j5XWfOW81",
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
                    exid = "aWIAv7909ody9t1kQQMe",
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
                    exid = "dmdFqRZcATGPv0N3cltR",
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