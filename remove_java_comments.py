#!/usr/bin/env python3

import re
import sys

def remove_comments_from_java_file(file_path):
    """Remove all comments and commented lines from Java file"""
    
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    
    # Split into lines for processing
    lines = content.split('\n')
    cleaned_lines = []
    in_multiline_comment = False
    
    for line in lines:
        stripped_line = line.strip()
        
        # Skip completely commented lines (starting with // or /* or *)
        if (stripped_line.startswith('//') or 
            stripped_line.startswith('/*') or 
            stripped_line.startswith('*') or
            stripped_line.startswith('*/')):
            continue
            
        # Handle multiline comments
        if '/*' in line and '*/' in line:
            # Single line multiline comment - remove it
            line = re.sub(r'/\*.*?\*/', '', line)
        elif '/*' in line:
            # Start of multiline comment
            line = line[:line.find('/*')]
            in_multiline_comment = True
        elif '*/' in line and in_multiline_comment:
            # End of multiline comment
            line = line[line.find('*/') + 2:]
            in_multiline_comment = False
        elif in_multiline_comment:
            # Inside multiline comment, skip this line
            continue
        
        # Remove single line comments (but preserve // inside strings)
        if '//' in line:
            # Simple approach: find // and check if it's inside quotes
            in_string = False
            escape_next = False
            for i, char in enumerate(line):
                if escape_next:
                    escape_next = False
                    continue
                if char == '\\':
                    escape_next = True
                    continue
                if char == '"' and not escape_next:
                    in_string = not in_string
                elif char == '/' and i + 1 < len(line) and line[i + 1] == '/' and not in_string:
                    line = line[:i].rstrip()
                    break
        
        # Only keep non-empty lines or lines with meaningful content
        if line.strip():
            cleaned_lines.append(line.rstrip())
    
    # Join lines back together
    cleaned_content = '\n'.join(cleaned_lines)
    
    # Remove excessive empty lines (more than 2 consecutive)
    cleaned_content = re.sub(r'\n{3,}', '\n\n', cleaned_content)
    
    return cleaned_content

def main():
    if len(sys.argv) != 2:
        print("Usage: python3 remove_comments.py <java_file>")
        sys.exit(1)
    
    file_path = sys.argv[1]
    
    try:
        cleaned_content = remove_comments_from_java_file(file_path)
        
        # Write back to file
        with open(file_path, 'w', encoding='utf-8') as file:
            file.write(cleaned_content)
        
        print(f"Successfully removed all comments from {file_path}")
        
    except Exception as e:
        print(f"Error processing file: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
