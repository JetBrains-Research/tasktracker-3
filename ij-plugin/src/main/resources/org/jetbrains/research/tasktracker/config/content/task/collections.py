# Function definitions for reference
def get_even_numbers_list():
    return [num for num in range(101) if num % 2 == 0]


def get_even_numbers_set():
    return {num for num in range(101) if num % 2 == 0}


# Calls and Assertions
def test_functions():
    # Call the get_even_numbers_list function and assert its correctness
    even_list_1 = get_even_numbers_list()
    assert even_list_1 == [0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38,
                           40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76,
                           78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100]
    even_list_2 = get_even_numbers_list()
    assert len(even_list_2) == 51  # Assert that the list contains exactly 51 elements

    # Call the get_even_numbers_set function and assert its correctness
    even_set_1 = get_even_numbers_set()
    assert even_set_1 == {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38,
                          40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76,
                          78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100}
    even_set_2 = get_even_numbers_set()
    assert 100 in even_set_2  # Assert that 100 is in the set


# Execute the test function to validate both functions
test_functions()