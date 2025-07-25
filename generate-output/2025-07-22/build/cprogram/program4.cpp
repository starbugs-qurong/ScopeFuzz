#include <iostream> 
//insertion-include-available
using namespace std;
 
int a9g23B3[5] = {6,3,3,5,3};
//insertion-global-available



template <bool> struct sa;

template <> struct sa<true> {
};


struct one   {
char x[1];
};

struct two   {
char x[2];
};

struct three {
char x[3];
};

struct four  {
char x[4];
};

struct five  {
char x[5];
};

struct six   {
char x[6];
};

struct seven {
char x[7];
};

struct eight {
char x[8];
};


struct A
{

A();

A(const volatile A&&);

};


A    source();

const          A  c_source();

volatile A  v_source();

const volatile A cv_source();



one   sink_5_12345(               A&);

two   sink_5_12345(const          A&);

three sink_5_12345(volatile       A&);

four  sink_5_12345(const volatile A&);

five  sink_5_12345(               A&&);


int test5_12345()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12345(v_source());

sink_5_12345(cv_source());

return 0;

}


one   sink_5_12346(               A&);

two   sink_5_12346(const          A&);

three sink_5_12346(volatile       A&);

four  sink_5_12346(const volatile A&);

six   sink_5_12346(const          A&&);


int test5_12346()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12346(v_source());

sink_5_12346(cv_source());

return 0;

}


one   sink_5_12347(               A&);

two   sink_5_12347(const          A&);

three sink_5_12347(volatile       A&);

four  sink_5_12347(const volatile A&);

seven sink_5_12347(volatile       A&&);


int test5_12347()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12347(cv_source());

return 0;

}


one   sink_5_12356(               A&);

two   sink_5_12356(const          A&);

three sink_5_12356(volatile       A&);

five  sink_5_12356(               A&&);

six   sink_5_12356(const          A&&);


int test5_12356()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12356(cva);

sink_5_12356(v_source());

sink_5_12356(cv_source());

return 0;

}


one   sink_5_12357(               A&);

two   sink_5_12357(const          A&);

three sink_5_12357(volatile       A&);

five  sink_5_12357(               A&&);

seven sink_5_12357(volatile       A&&);


int test5_12357()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12357(cva);

sink_5_12357(cv_source());

return 0;

}


one   sink_5_12358(               A&);

two   sink_5_12358(const          A&);

three sink_5_12358(volatile       A&);

five  sink_5_12358(               A&&);

eight sink_5_12358(const volatile A&&);


int test5_12358()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12358(cva);

return 0;

}


one   sink_5_12367(               A&);

two   sink_5_12367(const          A&);

three sink_5_12367(volatile       A&);

six   sink_5_12367(const          A&&);

seven sink_5_12367(volatile       A&&);


int test5_12367()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12367(cva);

sink_5_12367(source());

sink_5_12367(cv_source());

return 0;

}


one   sink_5_12368(               A&);

two   sink_5_12368(const          A&);

three sink_5_12368(volatile       A&);

six   sink_5_12368(const          A&&);

eight sink_5_12368(const volatile A&&);


int test5_12368()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12368(cva);

return 0;

}


one   sink_5_12378(               A&);

two   sink_5_12378(const          A&);

three sink_5_12378(volatile       A&);

seven sink_5_12378(volatile       A&&);

eight sink_5_12378(const volatile A&&);


int test5_12378()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12378(cva);

return 0;

}


one   sink_5_12456(               A&);

two   sink_5_12456(const          A&);

four  sink_5_12456(const volatile A&);

five  sink_5_12456(               A&&);

six   sink_5_12456(const          A&&);


int test5_12456()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12456(v_source());

sink_5_12456(cv_source());

return 0;

}


one   sink_5_12457(               A&);

two   sink_5_12457(const          A&);

four  sink_5_12457(const volatile A&);

five  sink_5_12457(               A&&);

seven sink_5_12457(volatile       A&&);


int test5_12457()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12457(cv_source());

return 0;

}


one   sink_5_12467(               A&);

two   sink_5_12467(const          A&);

four  sink_5_12467(const volatile A&);

six   sink_5_12467(const          A&&);

seven sink_5_12467(volatile       A&&);


int test5_12467()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12467(source());

sink_5_12467(cv_source());

return 0;

}


one   sink_5_12567(               A&);

two   sink_5_12567(const          A&);

five  sink_5_12567(               A&&);

six   sink_5_12567(const          A&&);

seven sink_5_12567(volatile       A&&);


int test5_12567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12567(va);

sink_5_12567(cva);

sink_5_12567(cv_source());

return 0;

}


one   sink_5_12568(               A&);

two   sink_5_12568(const          A&);

five  sink_5_12568(               A&&);

six   sink_5_12568(const          A&&);

eight sink_5_12568(const volatile A&&);


int test5_12568()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12568(va);

sink_5_12568(cva);

return 0;

}


one   sink_5_12578(               A&);

two   sink_5_12578(const          A&);

five  sink_5_12578(               A&&);

seven sink_5_12578(volatile       A&&);

eight sink_5_12578(const volatile A&&);


int test5_12578()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12578(va);

sink_5_12578(cva);

return 0;

}


one   sink_5_12678(               A&);

two   sink_5_12678(const          A&);

six   sink_5_12678(const          A&&);

seven sink_5_12678(volatile       A&&);

eight sink_5_12678(const volatile A&&);


int test5_12678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12678(va);

sink_5_12678(cva);

sink_5_12678(source());

return 0;

}


one   sink_5_13456(               A&);

three sink_5_13456(volatile       A&);

four  sink_5_13456(const volatile A&);

five  sink_5_13456(               A&&);

six   sink_5_13456(const          A&&);


int test5_13456()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13456(v_source());

sink_5_13456(cv_source());

return 0;

}


one   sink_5_13457(               A&);

three sink_5_13457(volatile       A&);

four  sink_5_13457(const volatile A&);

five  sink_5_13457(               A&&);

seven sink_5_13457(volatile       A&&);


int test5_13457()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13457(c_source());

sink_5_13457(cv_source());

return 0;

}


one   sink_5_13467(               A&);

three sink_5_13467(volatile       A&);

four  sink_5_13467(const volatile A&);

six   sink_5_13467(const          A&&);

seven sink_5_13467(volatile       A&&);


int test5_13467()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13467(source());

sink_5_13467(cv_source());

return 0;

}


one   sink_5_13567(               A&);

three sink_5_13567(volatile       A&);

five  sink_5_13567(               A&&);

six   sink_5_13567(const          A&&);

seven sink_5_13567(volatile       A&&);


int test5_13567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13567(ca);

sink_5_13567(cva);

sink_5_13567(cv_source());

return 0;

}


one   sink_5_13568(               A&);

three sink_5_13568(volatile       A&);

five  sink_5_13568(               A&&);

six   sink_5_13568(const          A&&);

eight sink_5_13568(const volatile A&&);


int test5_13568()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13568(ca);

sink_5_13568(cva);

return 0;

}


one   sink_5_13578(               A&);

three sink_5_13578(volatile       A&);

five  sink_5_13578(               A&&);

seven sink_5_13578(volatile       A&&);

eight sink_5_13578(const volatile A&&);


int test5_13578()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13578(ca);

sink_5_13578(cva);

return 0;

}


one   sink_5_13678(               A&);

three sink_5_13678(volatile       A&);

six   sink_5_13678(const          A&&);

seven sink_5_13678(volatile       A&&);

eight sink_5_13678(const volatile A&&);


int test5_13678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13678(ca);

sink_5_13678(cva);

sink_5_13678(source());

return 0;

}


one   sink_5_14567(               A&);

four  sink_5_14567(const volatile A&);

five  sink_5_14567(               A&&);

six   sink_5_14567(const          A&&);

seven sink_5_14567(volatile       A&&);


int test5_14567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_14567(cv_source());

return 0;

}


one   sink_5_14678(               A&);

four  sink_5_14678(const volatile A&);

six   sink_5_14678(const          A&&);

seven sink_5_14678(volatile       A&&);

eight sink_5_14678(const volatile A&&);


int test5_14678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_14678(source());

return 0;

}


one   sink_5_15678(               A&);

five  sink_5_15678(               A&&);

six   sink_5_15678(const          A&&);

seven sink_5_15678(volatile       A&&);

eight sink_5_15678(const volatile A&&);


int test5_15678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_15678(ca);

sink_5_15678(va);

sink_5_15678(cva);

return 0;

}


two   sink_5_23456(const          A&);

three sink_5_23456(volatile       A&);

four  sink_5_23456(const volatile A&);

five  sink_5_23456(               A&&);

six   sink_5_23456(const          A&&);


int test5_23456()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23456(a);

sink_5_23456(v_source());

sink_5_23456(cv_source());

return 0;

}


two   sink_5_23457(const          A&);

three sink_5_23457(volatile       A&);

four  sink_5_23457(const volatile A&);

five  sink_5_23457(               A&&);

seven sink_5_23457(volatile       A&&);


int test5_23457()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23457(a);

sink_5_23457(cv_source());

return 0;

}


two   sink_5_23458(const          A&);

three sink_5_23458(volatile       A&);

four  sink_5_23458(const volatile A&);

five  sink_5_23458(               A&&);

eight sink_5_23458(const volatile A&&);


int test5_23458()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23458(a);

return 0;

}


two   sink_5_23467(const          A&);

three sink_5_23467(volatile       A&);

four  sink_5_23467(const volatile A&);

six   sink_5_23467(const          A&&);

seven sink_5_23467(volatile       A&&);


int test5_23467()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23467(a);

sink_5_23467(source());

sink_5_23467(cv_source());

return 0;

}


two   sink_5_23468(const          A&);

three sink_5_23468(volatile       A&);

four  sink_5_23468(const volatile A&);

six   sink_5_23468(const          A&&);

eight sink_5_23468(const volatile A&&);


int test5_23468()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23468(a);

return 0;

}


two   sink_5_23478(const          A&);

three sink_5_23478(volatile       A&);

four  sink_5_23478(const volatile A&);

seven sink_5_23478(volatile       A&&);

eight sink_5_23478(const volatile A&&);


int test5_23478()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23478(a);

return 0;

}


two   sink_5_23567(const          A&);

three sink_5_23567(volatile       A&);

five  sink_5_23567(               A&&);

six   sink_5_23567(const          A&&);

seven sink_5_23567(volatile       A&&);


int test5_23567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23567(a);

sink_5_23567(cva);

sink_5_23567(cv_source());

return 0;

}


two   sink_5_23568(const          A&);

three sink_5_23568(volatile       A&);

five  sink_5_23568(               A&&);

six   sink_5_23568(const          A&&);

eight sink_5_23568(const volatile A&&);


int test5_23568()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23568(cva);

sink_5_23568(a);

return 0;

}


two   sink_5_23578(const          A&);

three sink_5_23578(volatile       A&);

five  sink_5_23578(               A&&);

seven sink_5_23578(volatile       A&&);

eight sink_5_23578(const volatile A&&);


int test5_23578()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23578(cva);

sink_5_23578(a);

return 0;

}


two   sink_5_23678(const          A&);

three sink_5_23678(volatile       A&);

six   sink_5_23678(const          A&&);

seven sink_5_23678(volatile       A&&);

eight sink_5_23678(const volatile A&&);


int test5_23678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23678(a);

sink_5_23678(cva);

sink_5_23678(source());

return 0;

}


two   sink_5_24567(const          A&);

four  sink_5_24567(const volatile A&);

five  sink_5_24567(               A&&);

six   sink_5_24567(const          A&&);

seven sink_5_24567(volatile       A&&);


int test5_24567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_24567(cv_source());

return 0;

}


two   sink_5_24678(const          A&);

four  sink_5_24678(const volatile A&);

six   sink_5_24678(const          A&&);

seven sink_5_24678(volatile       A&&);

eight sink_5_24678(const volatile A&&);


int test5_24678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_24678(source());

return 0;

}


two   sink_5_25678(const          A&);

five  sink_5_25678(               A&&);

six   sink_5_25678(const          A&&);

seven sink_5_25678(volatile       A&&);

eight sink_5_25678(const volatile A&&);


int test5_25678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_25678(va);

sink_5_25678(cva);

return 0;

}


three sink_5_34567(volatile       A&);

four  sink_5_34567(const volatile A&);

five  sink_5_34567(               A&&);

six   sink_5_34567(const          A&&);

seven sink_5_34567(volatile       A&&);


int test5_34567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_34567(cv_source());

return 0;

}


three sink_5_34678(volatile       A&);

four  sink_5_34678(const volatile A&);

six   sink_5_34678(const          A&&);

seven sink_5_34678(volatile       A&&);

eight sink_5_34678(const volatile A&&);


int test5_34678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_34678(source());

return 0;

}


three sink_5_35678(volatile       A&);

five  sink_5_35678(               A&&);

six   sink_5_35678(const          A&&);

seven sink_5_35678(volatile       A&&);

eight sink_5_35678(const volatile A&&);


int test5_35678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_35678(ca);

sink_5_35678(cva);

return 0;

}





template <bool> struct sa;

template <> struct sa<true> {
};


struct one   {
char x[1];
};

struct two   {
char x[2];
};

struct three {
char x[3];
};

struct four  {
char x[4];
};

struct five  {
char x[5];
};

struct six   {
char x[6];
};

struct seven {
char x[7];
};

struct eight {
char x[8];
};


struct A
{

A();

A(const volatile A&&);

};


A    source();

const          A  c_source();

volatile A  v_source();

const volatile A cv_source();



one   sink_5_12345(               A&);

two   sink_5_12345(const          A&);

three sink_5_12345(volatile       A&);

four  sink_5_12345(const volatile A&);

five  sink_5_12345(               A&&);


int test5_12345()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12345(v_source());

sink_5_12345(cv_source());

return 0;

}


one   sink_5_12346(               A&);

two   sink_5_12346(const          A&);

three sink_5_12346(volatile       A&);

four  sink_5_12346(const volatile A&);

six   sink_5_12346(const          A&&);


int test5_12346()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12346(v_source());

sink_5_12346(cv_source());

return 0;

}


one   sink_5_12347(               A&);

two   sink_5_12347(const          A&);

three sink_5_12347(volatile       A&);

four  sink_5_12347(const volatile A&);

seven sink_5_12347(volatile       A&&);


int test5_12347()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12347(cv_source());

return 0;

}


one   sink_5_12356(               A&);

two   sink_5_12356(const          A&);

three sink_5_12356(volatile       A&);

five  sink_5_12356(               A&&);

six   sink_5_12356(const          A&&);


int test5_12356()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12356(cva);

sink_5_12356(v_source());

sink_5_12356(cv_source());

return 0;

}


one   sink_5_12357(               A&);

two   sink_5_12357(const          A&);

three sink_5_12357(volatile       A&);

five  sink_5_12357(               A&&);

seven sink_5_12357(volatile       A&&);


int test5_12357()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12357(cva);

sink_5_12357(cv_source());

return 0;

}


one   sink_5_12358(               A&);

two   sink_5_12358(const          A&);

three sink_5_12358(volatile       A&);

five  sink_5_12358(               A&&);

eight sink_5_12358(const volatile A&&);


int test5_12358()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12358(cva);

return 0;

}


one   sink_5_12367(               A&);

two   sink_5_12367(const          A&);

three sink_5_12367(volatile       A&);

six   sink_5_12367(const          A&&);

seven sink_5_12367(volatile       A&&);


int test5_12367()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12367(cva);

sink_5_12367(source());

sink_5_12367(cv_source());

return 0;

}


one   sink_5_12368(               A&);

two   sink_5_12368(const          A&);

three sink_5_12368(volatile       A&);

six   sink_5_12368(const          A&&);

eight sink_5_12368(const volatile A&&);


int test5_12368()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12368(cva);

return 0;

}


one   sink_5_12378(               A&);

two   sink_5_12378(const          A&);

three sink_5_12378(volatile       A&);

seven sink_5_12378(volatile       A&&);

eight sink_5_12378(const volatile A&&);


int test5_12378()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12378(cva);

return 0;

}


one   sink_5_12456(               A&);

two   sink_5_12456(const          A&);

four  sink_5_12456(const volatile A&);

five  sink_5_12456(               A&&);

six   sink_5_12456(const          A&&);


int test5_12456()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12456(v_source());

sink_5_12456(cv_source());

return 0;

}


one   sink_5_12457(               A&);

two   sink_5_12457(const          A&);

four  sink_5_12457(const volatile A&);

five  sink_5_12457(               A&&);

seven sink_5_12457(volatile       A&&);


int test5_12457()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12457(cv_source());

return 0;

}


one   sink_5_12467(               A&);

two   sink_5_12467(const          A&);

four  sink_5_12467(const volatile A&);

six   sink_5_12467(const          A&&);

seven sink_5_12467(volatile       A&&);


int test5_12467()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12467(source());

sink_5_12467(cv_source());

return 0;

}


one   sink_5_12567(               A&);

two   sink_5_12567(const          A&);

five  sink_5_12567(               A&&);

six   sink_5_12567(const          A&&);

seven sink_5_12567(volatile       A&&);


int test5_12567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12567(va);

sink_5_12567(cva);

sink_5_12567(cv_source());

return 0;

}


one   sink_5_12568(               A&);

two   sink_5_12568(const          A&);

five  sink_5_12568(               A&&);

six   sink_5_12568(const          A&&);

eight sink_5_12568(const volatile A&&);


int test5_12568()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12568(va);

sink_5_12568(cva);

return 0;

}


one   sink_5_12578(               A&);

two   sink_5_12578(const          A&);

five  sink_5_12578(               A&&);

seven sink_5_12578(volatile       A&&);

eight sink_5_12578(const volatile A&&);


int test5_12578()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12578(va);

sink_5_12578(cva);

return 0;

}


one   sink_5_12678(               A&);

two   sink_5_12678(const          A&);

six   sink_5_12678(const          A&&);

seven sink_5_12678(volatile       A&&);

eight sink_5_12678(const volatile A&&);


int test5_12678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_12678(va);

sink_5_12678(cva);

sink_5_12678(source());

return 0;

}


one   sink_5_13456(               A&);

three sink_5_13456(volatile       A&);

four  sink_5_13456(const volatile A&);

five  sink_5_13456(               A&&);

six   sink_5_13456(const          A&&);


int test5_13456()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13456(v_source());

sink_5_13456(cv_source());

return 0;

}


one   sink_5_13457(               A&);

three sink_5_13457(volatile       A&);

four  sink_5_13457(const volatile A&);

five  sink_5_13457(               A&&);

seven sink_5_13457(volatile       A&&);


int test5_13457()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13457(c_source());

sink_5_13457(cv_source());

return 0;

}


one   sink_5_13467(               A&);

three sink_5_13467(volatile       A&);

four  sink_5_13467(const volatile A&);

six   sink_5_13467(const          A&&);

seven sink_5_13467(volatile       A&&);


int test5_13467()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13467(source());

sink_5_13467(cv_source());

return 0;

}


one   sink_5_13567(               A&);

three sink_5_13567(volatile       A&);

five  sink_5_13567(               A&&);

six   sink_5_13567(const          A&&);

seven sink_5_13567(volatile       A&&);


int test5_13567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13567(ca);

sink_5_13567(cva);

sink_5_13567(cv_source());

return 0;

}


one   sink_5_13568(               A&);

three sink_5_13568(volatile       A&);

five  sink_5_13568(               A&&);

six   sink_5_13568(const          A&&);

eight sink_5_13568(const volatile A&&);


int test5_13568()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13568(ca);

sink_5_13568(cva);

return 0;

}


one   sink_5_13578(               A&);

three sink_5_13578(volatile       A&);

five  sink_5_13578(               A&&);

seven sink_5_13578(volatile       A&&);

eight sink_5_13578(const volatile A&&);


int test5_13578()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13578(ca);

sink_5_13578(cva);

return 0;

}


one   sink_5_13678(               A&);

three sink_5_13678(volatile       A&);

six   sink_5_13678(const          A&&);

seven sink_5_13678(volatile       A&&);

eight sink_5_13678(const volatile A&&);


int test5_13678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_13678(ca);

sink_5_13678(cva);

sink_5_13678(source());

return 0;

}


one   sink_5_14567(               A&);

four  sink_5_14567(const volatile A&);

five  sink_5_14567(               A&&);

six   sink_5_14567(const          A&&);

seven sink_5_14567(volatile       A&&);


int test5_14567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_14567(cv_source());

return 0;

}


one   sink_5_14678(               A&);

four  sink_5_14678(const volatile A&);

six   sink_5_14678(const          A&&);

seven sink_5_14678(volatile       A&&);

eight sink_5_14678(const volatile A&&);


int test5_14678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_14678(source());

return 0;

}


one   sink_5_15678(               A&);

five  sink_5_15678(               A&&);

six   sink_5_15678(const          A&&);

seven sink_5_15678(volatile       A&&);

eight sink_5_15678(const volatile A&&);


int test5_15678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_15678(ca);

sink_5_15678(va);

sink_5_15678(cva);

return 0;

}


two   sink_5_23456(const          A&);

three sink_5_23456(volatile       A&);

four  sink_5_23456(const volatile A&);

five  sink_5_23456(               A&&);

six   sink_5_23456(const          A&&);


int test5_23456()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23456(a);

sink_5_23456(v_source());

sink_5_23456(cv_source());

return 0;

}


two   sink_5_23457(const          A&);

three sink_5_23457(volatile       A&);

four  sink_5_23457(const volatile A&);

five  sink_5_23457(               A&&);

seven sink_5_23457(volatile       A&&);


int test5_23457()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23457(a);

sink_5_23457(cv_source());

return 0;

}


two   sink_5_23458(const          A&);

three sink_5_23458(volatile       A&);

four  sink_5_23458(const volatile A&);

five  sink_5_23458(               A&&);

eight sink_5_23458(const volatile A&&);


int test5_23458()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23458(a);

return 0;

}


two   sink_5_23467(const          A&);

three sink_5_23467(volatile       A&);

four  sink_5_23467(const volatile A&);

six   sink_5_23467(const          A&&);

seven sink_5_23467(volatile       A&&);


int test5_23467()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23467(a);

sink_5_23467(source());

sink_5_23467(cv_source());

return 0;

}


two   sink_5_23468(const          A&);

three sink_5_23468(volatile       A&);

four  sink_5_23468(const volatile A&);

six   sink_5_23468(const          A&&);

eight sink_5_23468(const volatile A&&);


int test5_23468()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23468(a);

return 0;

}


two   sink_5_23478(const          A&);

three sink_5_23478(volatile       A&);

four  sink_5_23478(const volatile A&);

seven sink_5_23478(volatile       A&&);

eight sink_5_23478(const volatile A&&);


int test5_23478()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23478(a);

return 0;

}


two   sink_5_23567(const          A&);

three sink_5_23567(volatile       A&);

five  sink_5_23567(               A&&);

six   sink_5_23567(const          A&&);

seven sink_5_23567(volatile       A&&);


int test5_23567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23567(a);

sink_5_23567(cva);

sink_5_23567(cv_source());

return 0;

}


two   sink_5_23568(const          A&);

three sink_5_23568(volatile       A&);

five  sink_5_23568(               A&&);

six   sink_5_23568(const          A&&);

eight sink_5_23568(const volatile A&&);


int test5_23568()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23568(cva);

sink_5_23568(a);

return 0;

}


two   sink_5_23578(const          A&);

three sink_5_23578(volatile       A&);

five  sink_5_23578(               A&&);

seven sink_5_23578(volatile       A&&);

eight sink_5_23578(const volatile A&&);


int test5_23578()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23578(cva);

sink_5_23578(a);

return 0;

}


two   sink_5_23678(const          A&);

three sink_5_23678(volatile       A&);

six   sink_5_23678(const          A&&);

seven sink_5_23678(volatile       A&&);

eight sink_5_23678(const volatile A&&);


int test5_23678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_23678(a);

sink_5_23678(cva);

sink_5_23678(source());

return 0;

}


two   sink_5_24567(const          A&);

four  sink_5_24567(const volatile A&);

five  sink_5_24567(               A&&);

six   sink_5_24567(const          A&&);

seven sink_5_24567(volatile       A&&);


int test5_24567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_24567(cv_source());

return 0;

}


two   sink_5_24678(const          A&);

four  sink_5_24678(const volatile A&);

six   sink_5_24678(const          A&&);

seven sink_5_24678(volatile       A&&);

eight sink_5_24678(const volatile A&&);


int test5_24678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_24678(source());

return 0;

}


two   sink_5_25678(const          A&);

five  sink_5_25678(               A&&);

six   sink_5_25678(const          A&&);

seven sink_5_25678(volatile       A&&);

eight sink_5_25678(const volatile A&&);


int test5_25678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_25678(va);

sink_5_25678(cva);

return 0;

}


three sink_5_34567(volatile       A&);

four  sink_5_34567(const volatile A&);

five  sink_5_34567(               A&&);

six   sink_5_34567(const          A&&);

seven sink_5_34567(volatile       A&&);


int test5_34567()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_34567(cv_source());

return 0;

}


three sink_5_34678(volatile       A&);

four  sink_5_34678(const volatile A&);

six   sink_5_34678(const          A&&);

seven sink_5_34678(volatile       A&&);

eight sink_5_34678(const volatile A&&);


int test5_34678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_34678(source());

return 0;

}


three sink_5_35678(volatile       A&);

five  sink_5_35678(               A&&);

six   sink_5_35678(const          A&&);

seven sink_5_35678(volatile       A&&);

eight sink_5_35678(const volatile A&&);


int test5_35678()
{

A a;

const          A ca = a;

volatile A va;

const volatile A cva = a;

sink_5_35678(ca);

sink_5_35678(cva);

return 0;

}


  int J98b50HA = 6;
  short I81c36pL = 7;
 int zqDBz3G[5] = {1,0,2,3,3};
 template <int> void f1() {
}
    long J42Jl4Z0 (  ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
   int hb33321count=0;
for ( long f49FT1L = 6 ;
 (J98b50HA)|((a9g23B3[1])!=((-J98b50HA))) ;
 ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
hb33321count=hb33321count+1;
if(hb33321count>1){
break;
}
//insertion-scope-first-available
//insertion-scope-first-statement-available
  long iUS32x60 = 8 ;
  f49FT1L=((((f49FT1L==0)?1:(f49FT1L))==0)?1:(((f49FT1L==0)?1:(f49FT1L))))%((((f49FT1L==0)?1:(f49FT1L))==0)?1:(((f49FT1L==0)?1:(f49FT1L)))) ;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 int Y0970T8[6] = {( ( ( I81c36pL * ( 8 ) ) ) - ( ( J98b50HA + ( 6 ) ) ) ),( 6 ),5,0,8,8};
 cout << J98b50HA<< endl;
cout << I81c36pL<< endl;
for(int i=0;
i<5;
i++){
cout << zqDBz3G[i]<< endl;
}
for(int i=0;
i<5;
i++){
cout << a9g23B3[i]<< endl;
}
return 0;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
}
 int wgF60040 (  ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
//insertion-scope-first-available
//insertion-scope-first-statement-available
  int K19e0g5 = ( ( J98b50HA ) + ( ( ( 4 ) + ( I81c36pL ) ) ) ) ;
   cout << J98b50HA<< endl;
cout << I81c36pL<< endl;
for(int i=0;
i<5;
i++){
cout << zqDBz3G[i]<< endl;
}
for(int i=0;
i<5;
i++){
cout << a9g23B3[i]<< endl;
}
return 1;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 //insertion-global-available
int main() {
//insertion-scope-first-available
//insertion-scope-first-statement-available
//insertion-scope-first-available
//insertion-scope-first-statement-available
 //insertion-main-first-available
return test5_12356() + test5_12357() + test5_12367() + test5_12467() +
test5_12567() + test5_12678() + test5_13467() + test5_13567() +
test5_13678() + test5_13678() + test5_23456() + test5_23457() +
test5_23458() + test5_23467() + test5_23468() + test5_23478() +
test5_23567() + test5_23568() + test5_23578() + test5_23678() +
test5_24678() + test5_34678();



long Dsa4fYb=J42Jl4Z0();
int m73VeRzB=wgF60040();
cout << Dsa4fYb<< endl;
cout << m73VeRzB<< endl;
cout << J98b50HA<< endl;
cout << I81c36pL<< endl;
for(int i=0;
i<5;
i++){
cout << zqDBz3G[i]<< endl;
}
for(int i=0;
i<5;
i++){
cout << a9g23B3[i]<< endl;
}
//insertion-main-last-available
return test5_12356() + test5_12357() + test5_12367() + test5_12467() +
test5_12567() + test5_12678() + test5_13467() + test5_13567() +
test5_13678() + test5_13678() + test5_23456() + test5_23457() +
test5_23458() + test5_23467() + test5_23468() + test5_23478() +
test5_23567() + test5_23568() + test5_23578() + test5_23678() +
test5_24678() + test5_34678();



return 0;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
