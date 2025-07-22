#include <iostream> 
//insertion-include-available
using namespace std;
 
int pmAKU780 = 8;
//insertion-global-available

template<typename _Tp>
inline _Tp&&
movel(_Tp& __t)
{
 return static_cast<_Tp&&>(__t);
 }


struct S{
};


S l;

S const cl = l;

S r() {
 return l;
 }

S const cr() {
 return l;
 }

S & nl = l;

S const & ncl = l;

S && nr = movel(l);

S const && ncr = movel(l);

S & ul() {
 return l;
 }

S const & ucl() {
 return l;
 }

S && ur() {
 return movel(l);
 }

S const && ucr() {
 return movel(l);
 }


void l0001(const S&&) {
}


void l0010(S&&) {
}


void l0011(S&&) {
}

void l0011(const S&&);


void l0100(const S&) {
}


void l0101(const S&) {
}

void l0101(const S&&);


void l0110(const S&) {
}

void l0110(S&&);


void l0111(const S&) {
}

void l0111(S&&);

void l0111(const S&&);


void l1000(S&) {
}


void l1001(S&) {
}

void l1001(const S&&);


void l1010(S&) {
}

void l1010(S&&);


void l1011(S&) {
}

void l1011(S&&);

void l1011(const S&&);


void l1100(S&) {
}

void l1100(const S&);


void l1101(S&) {
}

void l1101(const S&);

void l1101(const S&&);


void l1110(S&) {
}

void l1110(const S&);

void l1110(S&&);


void l1111(S&) {
}

void l1111(const S&);

void l1111(S&&);

void l1111(const S&&);


void cl0001(const S&&) {
}


void cl0011(S&&);

void cl0011(const S&&) {
}


void cl0100(const S&) {
}


void cl0101(const S&) {
}

void cl0101(const S&&);


void cl0110(const S&) {
}

void cl0110(S&&);


void cl0111(const S&) {
}

void cl0111(S&&);

void cl0111(const S&&);


void cl1001(S&);

void cl1001(const S&&) {
}


void cl1011(S&);

void cl1011(S&&);

void cl1011(const S&&) {
}


void cl1100(S&);

void cl1100(const S&) {
}


void cl1101(S&);

void cl1101(const S&) {
}

void cl1101(const S&&);


void cl1110(S&);

void cl1110(const S&) {
}

void cl1110(S&&);


void cl1111(S&);

void cl1111(const S&) {
}

void cl1111(S&&);

void cl1111(const S&&);


void r0001(const S&&) {
}


void r0010(S&&) {
}


void r0011(S&&) {
}

void r0011(const S&&);


void r0100(const S&) {
}


void r0101(const S&);

void r0101(const S&&) {
}


void r0110(const S&);

void r0110(S&&) {
}


void r0111(const S&);

void r0111(S&&) {
}

void r0111(const S&&);


void r1001(S&);

void r1001(const S&&) {
}


void r1010(S&);

void r1010(S&&) {
}


void r1011(S&);

void r1011(S&&) {
}

void r1011(const S&&);


void r1100(S&);

void r1100(const S&) {
}


void r1101(S&);

void r1101(const S&);

void r1101(const S&&) {
}


void r1110(S&);

void r1110(const S&);

void r1110(S&&) {
}


void r1111(S&);

void r1111(const S&);

void r1111(S&&) {
}

void r1111(const S&&);


void cr0001(const S&&) {
}


void cr0011(S&&);

void cr0011(const S&&) {
}


void cr0100(const S&) {
}


void cr0101(const S&);

void cr0101(const S&&) {
}


void cr0110(const S&) {
}

void cr0110(S&&);


void cr0111(const S&);

void cr0111(S&&);

void cr0111(const S&&) {
}


void cr1001(S&);

void cr1001(const S&&) {
}


void cr1011(S&);

void cr1011(S&&);

void cr1011(const S&&) {
}


void cr1100(S&);

void cr1100(const S&) {
}


void cr1101(S&);

void cr1101(const S&);

void cr1101(const S&&) {
}


void cr1110(S&);

void cr1110(const S&) {
}

void cr1110(S&&);


void cr1111(S&);

void cr1111(const S&);

void cr1111(S&&);

void cr1111(const S&&) {
}


void nl0001(const S&&) {
}


void nl0010(S&&) {
}


void nl0011(S&&) {
}

void nl0011(const S&&);


void nl0100(const S&) {
}


void nl0101(const S&) {
}

void nl0101(const S&&);


void nl0110(const S&) {
}

void nl0110(S&&);


void nl0111(const S&) {
}

void nl0111(S&&);

void nl0111(const S&&);


void nl1000(S&) {
}


void nl1001(S&) {
}

void nl1001(const S&&);


void nl1010(S&) {
}

void nl1010(S&&);


void nl1011(S&) {
}

void nl1011(S&&);

void nl1011(const S&&);


void nl1100(S&) {
}

void nl1100(const S&);


void nl1101(S&) {
}

void nl1101(const S&);

void nl1101(const S&&);


void nl1110(S&) {
}

void nl1110(const S&);

void nl1110(S&&);


void nl1111(S&) {
}

void nl1111(const S&);

void nl1111(S&&);

void nl1111(const S&&);


void ncl0001(const S&&) {
}


void ncl0011(S&&);

void ncl0011(const S&&) {
}


void ncl0100(const S&) {
}


void ncl0101(const S&) {
}

void ncl0101(const S&&);


void ncl0110(const S&) {
}

void ncl0110(S&&);


void ncl0111(const S&) {
}

void ncl0111(S&&);

void ncl0111(const S&&);


void ncl1001(S&);

void ncl1001(const S&&) {
}


void ncl1011(S&);

void ncl1011(S&&);

void ncl1011(const S&&) {
}


void ncl1100(S&);

void ncl1100(const S&) {
}


void ncl1101(S&);

void ncl1101(const S&) {
}

void ncl1101(const S&&);


void ncl1110(S&);

void ncl1110(const S&) {
}

void ncl1110(S&&);


void ncl1111(S&);

void ncl1111(const S&) {
}

void ncl1111(S&&);

void ncl1111(const S&&);


void nr0001(const S&&) {
}


void nr0010(S&&) {
}


void nr0011(S&&) {
}

void nr0011(const S&&);


void nr0100(const S&) {
}


void nr0101(const S&) {
}

void nr0101(const S&&);


void nr0110(const S&) {
}

void nr0110(S&&);


void nr0111(const S&) {
}

void nr0111(S&&);

void nr0111(const S&&);


void nr1000(S&) {
}


void nr1001(S&) {
}

void nr1001(const S&&);


void nr1010(S&) {
}

void nr1010(S&&);


void nr1011(S&) {
}

void nr1011(S&&);

void nr1011(const S&&);


void nr1100(S&) {
}

void nr1100(const S&);


void nr1101(S&) {
}

void nr1101(const S&);

void nr1101(const S&&);


void nr1110(S&) {
}

void nr1110(const S&);

void nr1110(S&&);


void nr1111(S&) {
}

void nr1111(const S&);

void nr1111(S&&);

void nr1111(const S&&);


void ncr0001(const S&&) {
}


void ncr0011(S&&);

void ncr0011(const S&&) {
}


void ncr0100(const S&) {
}


void ncr0101(const S&) {
}

void ncr0101(const S&&);


void ncr0110(const S&) {
}

void ncr0110(S&&);


void ncr0111(const S&) {
}

void ncr0111(S&&);

void ncr0111(const S&&);


void ncr1001(S&);

void ncr1001(const S&&) {
}


void ncr1011(S&);

void ncr1011(S&&);

void ncr1011(const S&&) {
}


void ncr1100(S&);

void ncr1100(const S&) {
}


void ncr1101(S&);

void ncr1101(const S&) {
}

void ncr1101(const S&&);


void ncr1110(S&);

void ncr1110(const S&) {
}

void ncr1110(S&&);


void ncr1111(S&);

void ncr1111(const S&) {
}

void ncr1111(S&&);

void ncr1111(const S&&);


void ul0001(const S&&) {
}


void ul0010(S&&) {
}


void ul0011(S&&) {
}

void ul0011(const S&&);


void ul0100(const S&) {
}


void ul0101(const S&) {
}

void ul0101(const S&&);


void ul0110(const S&) {
}

void ul0110(S&&);


void ul0111(const S&) {
}

void ul0111(S&&);

void ul0111(const S&&);


void ul1000(S&) {
}


void ul1001(S&) {
}

void ul1001(const S&&);


void ul1010(S&) {
}

void ul1010(S&&);


void ul1011(S&) {
}

void ul1011(S&&);

void ul1011(const S&&);


void ul1100(S&) {
}

void ul1100(const S&);


void ul1101(S&) {
}

void ul1101(const S&);

void ul1101(const S&&);


void ul1110(S&) {
}

void ul1110(const S&);

void ul1110(S&&);


void ul1111(S&) {
}

void ul1111(const S&);

void ul1111(S&&);

void ul1111(const S&&);


void ucl0001(const S&&) {
}


void ucl0011(S&&);

void ucl0011(const S&&) {
}


void ucl0100(const S&) {
}


void ucl0101(const S&) {
}

void ucl0101(const S&&);


void ucl0110(const S&) {
}

void ucl0110(S&&);


void ucl0111(const S&) {
}

void ucl0111(S&&);

void ucl0111(const S&&);


void ucl1001(S&);

void ucl1001(const S&&) {
}


void ucl1011(S&);

void ucl1011(S&&);

void ucl1011(const S&&) {
}


void ucl1100(S&);

void ucl1100(const S&) {
}


void ucl1101(S&);

void ucl1101(const S&) {
}

void ucl1101(const S&&);


void ucl1110(S&);

void ucl1110(const S&) {
}

void ucl1110(S&&);


void ucl1111(S&);

void ucl1111(const S&) {
}

void ucl1111(S&&);

void ucl1111(const S&&);


void ur0001(const S&&) {
}


void ur0010(S&&) {
}


void ur0011(S&&) {
}

void ur0011(const S&&);


void ur0100(const S&) {
}


void ur0101(const S&);

void ur0101(const S&&) {
}


void ur0110(const S&);

void ur0110(S&&) {
}


void ur0111(const S&);

void ur0111(S&&) {
}

void ur0111(const S&&);


void ur1001(S&);

void ur1001(const S&&) {
}


void ur1010(S&);

void ur1010(S&&) {
}


void ur1011(S&);

void ur1011(S&&) {
}

void ur1011(const S&&);


void ur1100(S&);

void ur1100(const S&) {
}


void ur1101(S&);

void ur1101(const S&);

void ur1101(const S&&) {
}


void ur1110(S&);

void ur1110(const S&);

void ur1110(S&&) {
}


void ur1111(S&);

void ur1111(const S&);

void ur1111(S&&) {
}

void ur1111(const S&&);


void ucr0001(const S&&) {
}


void ucr0011(S&&);

void ucr0011(const S&&) {
}


void ucr0100(const S&) {
}


void ucr0101(const S&);

void ucr0101(const S&&) {
}


void ucr0110(const S&) {
}

void ucr0110(S&&);


void ucr0111(const S&);

void ucr0111(S&&);

void ucr0111(const S&&) {
}


void ucr1001(S&);

void ucr1001(const S&&) {
}


void ucr1011(S&);

void ucr1011(S&&);

void ucr1011(const S&&) {
}


void ucr1100(S&);

void ucr1100(const S&) {
}


void ucr1101(S&);

void ucr1101(const S&);

void ucr1101(const S&&) {
}


void ucr1110(S&);

void ucr1110(const S&) {
}

void ucr1110(S&&);


void ucr1111(S&);

void ucr1111(const S&);

void ucr1111(S&&);

void ucr1111(const S&&) {
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


  long kagA3F20 = 2;
 short W4104iPt = 8;
  int i25k95p[9] = {3,5,2,3,8,0,6,9,7};
  template < typename T > struct T5W049w5 {
 int ck9JyVc7 = 9;
 int X1y150H[9] = {0,9,0,1,1,1,0,3,3};
 void c43rjFFt ( int CPSp02v ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
//insertion-scope-first-available
//insertion-scope-first-statement-available
  int n48BTj6[4] = {( - ck9JyVc7 ),( ( W4104iPt ) + ( ( W4104iPt ) ) ),0,0};
 int U54M2S7count=0;
while ( (kagA3F20)<=(((((((((kagA3F20==0)?1:(kagA3F20))==0)?1:(((kagA3F20==0)?1:(kagA3F20)))))%(((((kagA3F20==0)?1:(kagA3F20))==0)?1:(((kagA3F20==0)?1:(kagA3F20)))))))*(((((((kagA3F20==0)?1:(kagA3F20))==0)?1:(((kagA3F20==0)?1:(kagA3F20)))))%(((((kagA3F20==0)?1:(kagA3F20))==0)?1:(((kagA3F20==0)?1:(kagA3F20))))))))) ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
U54M2S7count=U54M2S7count+1;
if(U54M2S7count>1){
break;
}
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int d567h2SA[6] = {( ( W4104iPt ) - ( W4104iPt ) ),5,7,3,6,6};
 int cXF77684 = ( W4104iPt * ( ( W4104iPt ) ) ) ;
 int wf840Agcount=0;
for ( int Kd2r7705 = ( ( W4104iPt ) * ( ( ( 5 ) ) ) ) ;
 (ck9JyVc7)<(((W4104iPt*(W4104iPt)))) ;
 cXF77684=((((ck9JyVc7==0)?1:(ck9JyVc7))==0)?1:(((ck9JyVc7==0)?1:(ck9JyVc7))))/((((cXF77684==0)?1:(cXF77684))==0)?1:(((cXF77684==0)?1:(cXF77684)))) ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
wf840Agcount=wf840Agcount+1;
if(wf840Agcount>1){
break;
}
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int eE161SL[8] = {( ( ( 3 ) ) + ( ( W4104iPt ) ) ),( + CPSp02v ),6,4,5,6,4,7};
  cXF77684&=(0) ;
 long zvR1oKo2 = ( kagA3F20 + ( ( (  ((((kagA3F20==0)?1:(kagA3F20))==0)?1:(((kagA3F20==0)?1:(kagA3F20)))) ) / (  ((((kagA3F20==0)?1:(kagA3F20))==0)?1:(((kagA3F20==0)?1:(kagA3F20)))) ) ) ) ) ;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
  //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
  return;
 cout << CPSp02v<< endl;
cout << kagA3F20<< endl;
cout << W4104iPt<< endl;
for(int i=0;
i<9;
i++){
cout << i25k95p[i]<< endl;
}
//insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 };
  template < typename T > class iz0K7Gdw {
 public : long E091h4p = 2;
 };
 void X63b470Q ( int * A05h5959 , int SV8480l9 [ ] ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
  int q5C68e64 = pmAKU780 ;
 pmAKU780^=((8)-(2)) ;
 W4104iPt^=(3) ;
 for(int i=0;
i<10;
i++){
cout << SV8480l9[i]<< endl;
}
cout << kagA3F20<< endl;
cout << W4104iPt<< endl;
for(int i=0;
i<9;
i++){
cout << i25k95p[i]<< endl;
}
cout << pmAKU780<< endl;
return;
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
int Tqx117Z = 5;
int *o2g1AIC2 = &pmAKU780;
int V9NG8253[10] = {9};
l0100(l);

l0101(l);

l0110(l);

l0111(l);

l1000(l);

l1001(l);

l1010(l);

l1011(l);

l1100(l);

l1101(l);

l1110(l);

l1111(l);

cl0100(cl);

cl0101(cl);

cl0110(cl);

cl0111(cl);

cl1100(cl);

cl1101(cl);

cl1110(cl);

cl1111(cl);

r0001(r());

r0010(r());

r0011(r());

r0100(r());

r0101(r());

r0110(r());

r0111(r());

r1001(r());

r1010(r());

r1011(r());

r1100(r());

r1101(r());

r1110(r());

r1111(r());

cr0001(cr());

cr0011(cr());

cr0100(cr());

cr0101(cr());

cr0110(cr());

cr0111(cr());

cr1001(cr());

cr1011(cr());

cr1100(cr());

cr1101(cr());

cr1110(cr());

cr1111(cr());

nl0100(nl);

nl0101(nl);

nl0110(nl);

nl0111(nl);

nl1000(nl);

nl1001(nl);

nl1010(nl);

nl1011(nl);

nl1100(nl);

nl1101(nl);

nl1110(nl);

nl1111(nl);

ncl0100(ncl);

ncl0101(ncl);

ncl0110(ncl);

ncl0111(ncl);

ncl1100(ncl);

ncl1101(ncl);

ncl1110(ncl);

ncl1111(ncl);

nr0100(nr);

nr0101(nr);

nr0110(nr);

nr0111(nr);

nr1000(nr);

nr1001(nr);

nr1010(nr);

nr1011(nr);

nr1100(nr);

nr1101(nr);

nr1110(nr);

nr1111(nr);

ncr0100(ncr);

ncr0101(ncr);

ncr0110(ncr);

ncr0111(ncr);

ncr1100(ncr);

ncr1101(ncr);

ncr1110(ncr);

ncr1111(ncr);

ul0100(ul());

ul0101(ul());

ul0110(ul());

ul0111(ul());

ul1000(ul());

ul1001(ul());

ul1010(ul());

ul1011(ul());

ul1100(ul());

ul1101(ul());

ul1110(ul());

ul1111(ul());

ucl0100(ucl());

ucl0101(ucl());

ucl0110(ucl());

ucl0111(ucl());

ucl1100(ucl());

ucl1101(ucl());

ucl1110(ucl());

ucl1111(ucl());

ur0001(ur());

ur0010(ur());

ur0011(ur());

ur0100(ur());

ur0101(ur());

ur0110(ur());

ur0111(ur());

ur1001(ur());

ur1010(ur());

ur1011(ur());

ur1100(ur());

ur1101(ur());

ur1110(ur());

ur1111(ur());

ucr0001(ucr());

ucr0011(ucr());

ucr0100(ucr());

ucr0101(ucr());

ucr0110(ucr());

ucr0111(ucr());

ucr1001(ucr());

ucr1011(ucr());

ucr1100(ucr());

ucr1101(ucr());

ucr1110(ucr());

ucr1111(ucr());


return 0;



T5W049w5<int> ePhPR5x8;
iz0K7Gdw<int> tq5f7z69;
ePhPR5x8.c43rjFFt(Tqx117Z);
X63b470Q(o2g1AIC2,V9NG8253);
cout << Tqx117Z<< endl;
for(int i=0;
i<1;
i++){
cout << V9NG8253[i]<< endl;
}
cout << kagA3F20<< endl;
cout << W4104iPt<< endl;
for(int i=0;
i<9;
i++){
cout << i25k95p[i]<< endl;
}
cout << pmAKU780<< endl;
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
