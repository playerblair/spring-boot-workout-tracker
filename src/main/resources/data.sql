-- Insert cardio exercises
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Running', 'Running at a steady pace on a flat surface or treadmill', 'CARDIO', NULL),
('Cycling', 'Pedaling on a stationary bike or outdoor bicycle', 'CARDIO', NULL),
('Jump Rope', 'Skipping rope exercise for cardiovascular endurance', 'CARDIO', NULL),
('Swimming', 'Full body cardio exercise performed in water', 'CARDIO', NULL),
('Elliptical Trainer', 'Low-impact cardio workout on an elliptical machine', 'CARDIO', NULL),
('Rowing', 'Using a rowing machine for full-body cardio', 'CARDIO', NULL),
('Stair Climber', 'Climbing on a stair machine for leg strength and cardio', 'CARDIO', 'LEGS'),
('Burpees', 'Full body exercise combining squat, push-up, and jump', 'CARDIO', NULL),
('Mountain Climbers', 'Dynamic plank exercise engaging core and building cardio endurance', 'CARDIO', 'CORE');

-- Insert strength exercises - Chest
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Bench Press', 'Lying on a bench and pushing weight upward with both arms', 'STRENGTH', 'CHEST'),
('Push-Up', 'Raising and lowering body using arms while maintaining rigid posture', 'STRENGTH', 'CHEST'),
('Dumbbell Fly', 'Lying on bench with arms extended to sides, raising dumbbells in an arc motion', 'STRENGTH', 'CHEST'),
('Incline Press', 'Bench press performed on an inclined bench to target upper chest', 'STRENGTH', 'CHEST'),
('Decline Press', 'Bench press performed on a declined bench to target lower chest', 'STRENGTH', 'CHEST'),
('Cable Crossover', 'Using cable pulleys to bring arms together in front of chest', 'STRENGTH', 'CHEST');

-- Insert strength exercises - Back
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Pull-Up', 'Lifting body weight by pulling up on a bar', 'STRENGTH', 'BACK'),
('Lat Pulldown', 'Pulling weighted bar down to chest level while seated', 'STRENGTH', 'BACK'),
('Bent-Over Row', 'Bending at waist and pulling weight to lower chest', 'STRENGTH', 'BACK'),
('Deadlift', 'Lifting barbell from ground by extending hips and straightening back', 'STRENGTH', 'BACK'),
('T-Bar Row', 'Rowing exercise using a T-bar apparatus to target middle back', 'STRENGTH', 'BACK'),
('Seated Cable Row', 'Pulling cable attachment towards abdomen while seated', 'STRENGTH', 'BACK');

-- Insert strength exercises - Legs
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Squat', 'Bending knees and lowering hips from standing position', 'STRENGTH', 'LEGS'),
('Leg Press', 'Pushing weight away using legs while seated in machine', 'STRENGTH', 'LEGS'),
('Lunge', 'Stepping forward and lowering body until both knees form 90-degree angles', 'STRENGTH', 'LEGS'),
('Leg Extension', 'Extending legs against resistance while seated', 'STRENGTH', 'LEGS'),
('Leg Curl', 'Curling legs against resistance while lying or seated', 'STRENGTH', 'LEGS'),
('Calf Raise', 'Rising onto toes against resistance', 'STRENGTH', 'LEGS'),
('Romanian Deadlift', 'Deadlift variation targeting hamstrings with minimal knee bend', 'STRENGTH', 'LEGS'),
('Bulgarian Split Squat', 'Single leg squat with rear foot elevated on bench', 'STRENGTH', 'LEGS');

-- Insert strength exercises - Arms
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Bicep Curl', 'Bending elbow to raise weight towards shoulder', 'STRENGTH', 'ARMS'),
('Tricep Extension', 'Extending elbow against resistance to work triceps', 'STRENGTH', 'ARMS'),
('Hammer Curl', 'Bicep curl with neutral grip (palms facing each other)', 'STRENGTH', 'ARMS'),
('Skull Crusher', 'Lying tricep extension bringing weight to forehead level', 'STRENGTH', 'ARMS'),
('Dip', 'Lowering and raising body using arms while suspended between parallel bars', 'STRENGTH', 'ARMS'),
('Wrist Curl', 'Flexing wrists to work forearm muscles', 'STRENGTH', 'ARMS'),
('Chin-Up', 'Pull-up variation with palms facing body to target biceps', 'STRENGTH', 'ARMS');

-- Insert strength exercises - Shoulders
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Overhead Press', 'Pressing weight overhead while standing or seated', 'STRENGTH', 'SHOULDERS'),
('Lateral Raise', 'Raising arms to sides against resistance', 'STRENGTH', 'SHOULDERS'),
('Front Raise', 'Raising arms to front against resistance', 'STRENGTH', 'SHOULDERS'),
('Reverse Fly', 'Pulling arms back against resistance while bent forward', 'STRENGTH', 'SHOULDERS'),
('Shrug', 'Raising shoulders toward ears against resistance', 'STRENGTH', 'SHOULDERS'),
('Face Pull', 'Pulling rope attachment to face level with elbows high', 'STRENGTH', 'SHOULDERS');

-- Insert strength exercises - Core
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Crunch', 'Lifting upper body off ground using abdominal muscles', 'STRENGTH', 'CORE'),
('Plank', 'Holding push-up position with body weight on forearms', 'STRENGTH', 'CORE'),
('Russian Twist', 'Rotating torso while seated with feet off ground', 'STRENGTH', 'CORE'),
('Leg Raise', 'Lifting legs using abdominal muscles while lying on back', 'STRENGTH', 'CORE'),
('Side Plank', 'Holding body rigid and supported on one forearm and side of foot', 'STRENGTH', 'CORE'),
('Ab Rollout', 'Using ab wheel to extend and contract core muscles', 'STRENGTH', 'CORE');

-- Insert flexibility exercises
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Hamstring Stretch', 'Stretching back of thigh by reaching for toes', 'FLEXIBILITY', 'LEGS'),
('Quad Stretch', 'Stretching front of thigh by pulling foot toward buttocks', 'FLEXIBILITY', 'LEGS'),
('Shoulder Stretch', 'Pulling arm across chest to stretch shoulder', 'FLEXIBILITY', 'SHOULDERS'),
('Child''s Pose', 'Kneeling stretch with arms extended forward and hips back', 'FLEXIBILITY', 'BACK'),
('Cobra Stretch', 'Lying face down and pushing upper body up to stretch abdomen', 'FLEXIBILITY', 'CORE'),
('Hip Flexor Stretch', 'Lunge position with emphasis on stretching hip of rear leg', 'FLEXIBILITY', 'LEGS'),
('Butterfly Stretch', 'Seated with soles of feet together to stretch inner thighs', 'FLEXIBILITY', 'LEGS'),
('Tricep Stretch', 'Reaching arm behind head and applying pressure to elbow', 'FLEXIBILITY', 'ARMS'),
('Cat-Cow Stretch', 'Moving between arching and rounding back while on hands and knees', 'FLEXIBILITY', 'BACK'),
('Neck Stretch', 'Gently pulling head to each side to stretch neck muscles', 'FLEXIBILITY', NULL);

---- Insert balance/functional exercises
INSERT INTO exercise (name, description, category, muscle_group) VALUES
('Single Leg Stand', 'Balancing on one leg to improve stability', 'BALANCE', 'LEGS'),
('Bosu Ball Squat', 'Performing squat while standing on an unstable surface', 'BALANCE', 'LEGS'),
('Medicine Ball Twist', 'Rotating torso while holding medicine ball', 'FUNCTIONAL', 'CORE'),
('Kettlebell Swing', 'Swinging kettlebell using hip hinge movement', 'FUNCTIONAL', 'FULL_BODY'),
('Battle Ropes', 'Creating waves with heavy ropes for cardiovascular and strength training', 'FUNCTIONAL', 'FULL_BODY'),
('Box Jump', 'Jumping onto elevated platform from standing position', 'FUNCTIONAL', 'LEGS'),
('TRX Row', 'Rowing exercise using suspension trainer', 'FUNCTIONAL', 'BACK'),
('Farmer''s Walk', 'Carrying heavy weights while walking', 'FUNCTIONAL', 'FULL_BODY');