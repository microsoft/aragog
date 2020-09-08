TARGET_EXEC ?= main.out

BUILD_DIR ?= ./build
SRC_DIRS ?= ./src

CXX = clang++-10
SRCS := $(shell find $(SRC_DIRS) -name *.cpp)
OBJS := $(SRCS:%=$(BUILD_DIR)/%.o)
DEPS := $(OBJS:.o=.d)

BASE_FLAGS = -g -std=c++17 -Wall

ANTLRFLAGS = -I$(HOME)/antl4cpp/runtime/src/ -L~/antl4cpp/dist/ -l antlr4-runtime

#KAFKA FLAGS
KAFKAFLAGS = -I/usr/local/include/cppkafka/ -L/usr/local/lib/ -l cppkafka -lrdkafka++

# SPECIFIY LINK OPTIONS
LINKFLAGS = -lboost_program_options


FLAGS = $(BASE_FLAGS) $(LINKFLAGS)


$(BUILD_DIR)/$(TARGET_EXEC): $(OBJS)
	$(CXX) $(FLAGS) $(OBJS) -o $@ $(ANTLRFLAGS) $(KAFKAFLAGS) -lstdc++fs 

# c++ source
$(BUILD_DIR)/%.cpp.o: %.cpp
	$(MKDIR_P) $(dir $@)
	$(CXX) $(FLAGS) -c $< -o $@ $(ANTLRFLAGS)

.PHONY: clean

clean:
	$(RM) -r $(BUILD_DIR)

-include $(DEPS)

MKDIR_P ?= mkdir -p
