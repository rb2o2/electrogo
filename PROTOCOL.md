# ROLE
Your role is Expert Senior Java developer 

# WORKFLOW
- Look into src/main/java/io/github/rb2o2/esg folder, study existing codebase there
- Work on tasks in small iterations, ideally not more than 500-1000 bytes of changes in code.
- To achieve this you must decompose tasks provided in @ACTIVE_TASKS.md into small parts, 
each of which must be described in @CHUNKS.md and must be commited after decomposition with chunk subtask description as a git commit message. If a task can be carried out with less than 500 bytes of code or less than 3 bash commands, then do it, and do not decompose, just put the task to @CHUNKS.md and commit. See #task-workflow
- project should be runnable, and must not contain runtime errors after each task is completed
- Try hard to not introduce errors which prevent running app
- All logic must be implemented respecting SOLID and Effective Java practices

# BEST PRACTICES GUIDE
- Avoid duplicating code, factor out duplicates into single modules (functions, classes - as appropriate), if several variants of similar code exist, consider writing single parametrized procedure/function/routine - as seems fit for particular programming language and stack 
- Do not overengineer: simple tasks should have simple solutions
- Respect SOLID principles when laying out program structure
- Start with simple but fundamental solution, then try to tweak and improve to meet requirements
- Also, consult with @BestPractices.md for specific coding best practices

# Task workflow
@ACTIVE_TASKS.md is a read-only file and contains tasks which you should precisely execute. Example file structure:
```markdown
# REFACTOR 1
ignore requirement on changes size limit for this task. look into @src/main.js file. Figure out how to refactor it into more structured and code. Factor out some repeating code, while keeping the logic intact
# Check creation of commits
check git log for creation of commits after each chunk  
```
CHUNKS.md example structure after decomposing task from ACTIVE_TASKS.md:
```markdown
# REFACTOR
- [x] you should fill this with first subtask. this chunk is checkmarked as done  
- [+] you should fill this with second subtask. this chunk is checkmarked as done too
- [ ] etc. the checkmark denotes incomplete chunk
- [!] this is a buggy chunk, i.e. first occurence of a bug in a series of commits
- [-] you should fill this with last subtask i.e. chunk (also checkmarked as incomplete)
# check creation of commits
- [ ] check if git log has matching commit messages as in CHUNKS.md 
- [ ] check this very message gets in git log last message
```
- Do not use emojis as checkmarks. Use specified marks instead
- You should not do a task or a chunk that has been completed i.e. put into CHUNKS.md and committed within the codebase.
- Code for a chunk and the chunk in CHUNKS.md should be commited together.

# QA WORKFLOW
- Ask the user to provide a list of bug descriptions.
- Write these descriptions to an ISSUES.md file, creating it if it doesn't already exist (see #issues).
- Check out the previous commit and run `npm run dev`. Ask the user to manually verify whether the bugs still persist.
  - If any bugs are present, mark the corresponding sections in CHUNKS.md as buggy (see #task-workflow).
  - Continue checking out earlier commits, asking the user to identify which bugs remain in each.
  - For each bug listed in ISSUES.md, record the commit hashes and messages for the commits that introduced those bugs.
  - Repeat the process with earlier commits until the user confirms that no bugs are present in the current checkout. At this point, the checked-out commit should be free of bugs, and all bugs listed in ISSUES.md should have associated commit information indicating where they were introduced.
- For each bug in ISSUES.md, check out the commit that introduced it, create a new branch named `bugfix-<commit-hash>` (e.g., `bugfix-abcdef6`, where `abcdef6` is the commit hash), and add this bugfix branch name to the corresponding entry in ISSUES.md.

# issues
ISSUES.md files should contain sections named with user-provided bug descriptions. In a section there should be commit hash and commit message of a commit with chunk which first introduced the issue. Example markdown as follows:
```markdown
# INCORRECT BEHAVIOUR WHEN CLICKING roll button rapidly
- git commit message when the bug appeared
- commit-hash: abcdef6
- branch: bugfix-abcdef6
# SHOULD RESIZE game ui when screen is rotated on mobile
- last commit message
- commit-hash: bf7acee
- branch: bugfix-bf7acee
``` 
