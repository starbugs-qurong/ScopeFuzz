# Makefile for build/cprogram

CXX ?= g++-10
COMMON_CFLAGS = -w -fsanitize=undefined -fno-sanitize-recover=all
OPT_CFLAGS ?= -O
OPT_DIAG ?= 

CXXFLAGS = $(COMMON_CFLAGS) $(OPT_CFLAGS) $(OPT_DIAG)
SOURCES = $(wildcard build/cprogram/*.cpp)

# 确定优化级别
ifneq (,$(findstring -O0,$(CXXFLAGS)))
  OPTIMIZATION = O0
else ifneq (,$(findstring -O1,$(CXXFLAGS)))
  OPTIMIZATION = O1
else ifneq (,$(findstring -O2,$(CXXFLAGS)))
  OPTIMIZATION = O2
else ifneq (,$(findstring -O3,$(CXXFLAGS)))
  OPTIMIZATION = O3
else ifneq (,$(findstring -Os,$(CXXFLAGS)))
  OPTIMIZATION = Os
else
  OPTIMIZATION = withoutO2
endif

COMPILER_NAME = $(basename $(notdir $(CXX)))
COMPILER_DIR = build/compiler/$(COMPILER_NAME)-$(OPTIMIZATION)
EXECUTABLES_DIR = $(COMPILER_DIR)/executables
FAILURES_DIR = $(COMPILER_DIR)/compiler_failures
EXECUTABLES = $(SOURCES:build/cprogram/%.cpp=$(EXECUTABLES_DIR)/%)

all: $(EXECUTABLES)

$(EXECUTABLES_DIR)/%: build/cprogram/%.cpp | $(EXECUTABLES_DIR) $(FAILURES_DIR)
	@-timeout 60s $(CXX) $(CXXFLAGS) $< -o $@ 2>/dev/null; \
	status=$$?; \
	if [ $$status -eq 124 ]; then \
		echo $(notdir $<) >> $(FAILURES_DIR)/compile_timeouts.log; \
	elif [ $$status -ne 0 ]; then \
		echo $(notdir $<) >> $(FAILURES_DIR)/compile_failures.log; \
	fi

$(EXECUTABLES_DIR) $(FAILURES_DIR):
	mkdir -p $@

clean:
	rm -rf $(COMPILER_DIR)/*