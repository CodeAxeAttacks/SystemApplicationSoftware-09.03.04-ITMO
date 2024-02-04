import subprocess

tests = ["vale_de_este", "adios_payasos", "ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm_ummuimumimmu_imimumumuumm", "order2671", ""]
stdout_check = ["the name of some wine idk", "bye-bye clowns in spanish", "my nickname in steam", "the number of my last order in kfc", ""]
stderr_check = ["", "", "It is too long string!", "", "The string is not found!"]

path = "./program"
tests_counter = 0
passed_tests_counter = 0
print("----------------------------")
for i in range(len(tests)):
   process = subprocess.run(path, input=tests[i], text=True, capture_output=True)
   stdout = str(process.stdout.encode())[2:-5]
   stderr = process.stderr.strip()
   tests_counter += 1
   if (stdout != stdout_check[i]) and (stderr != stderr_check[i]):
       print("test #" + str(tests_counter) + " failed:\nprogram stdout: " + stdout + "\nexpected stdout: " + stdout_check[i] + "\nprogram stderr: " + stderr + "\nexpected stderr: " + stderr_check[i])
   else:
       print("test #" + str(tests_counter) + " passed!")
       passed_tests_counter += 1
   print("----------------------------")
print("tests passed: " + str(passed_tests_counter) + " of " + str(len(tests)))
print("----------------------------")
