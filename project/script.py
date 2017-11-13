f = open('tmp.txt','w')
with open ('file.txt') as input:
    for line in input:
        for c in line:
            if c == '=':
                c = c.replace('=','')
            elif c == '(':
                c= c.replace('(','')
            elif c == ')':
                c= c.replace(')','')
            elif c == '+':
                c = c.replace('+',',')
            f.write(c)
f.close()

